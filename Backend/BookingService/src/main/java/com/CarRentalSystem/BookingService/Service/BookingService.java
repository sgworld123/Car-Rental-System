package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Dto.BookingByIdResponse;
import com.CarRentalSystem.BookingService.Dto.BookingRequestDto;
import com.CarRentalSystem.BookingService.Dto.BookingResponseDto;
import com.CarRentalSystem.BookingService.Dto.RequestId;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import com.CarRentalSystem.BookingService.Utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final Validation validation;
    private final BookingRepository bookingRepository;
    private final RedisTemplate redisTemplate;
    @Transactional
    public BookingResponseDto createBooking(String userId ,BookingRequestDto bookingRequestDto)
    {
        boolean isAvailable = validation.isVehicleAvailable(
                bookingRequestDto.getVehicleId(),
                bookingRequestDto.getFromDate(),
                bookingRequestDto.getToDate()
        ) && validation.isVehicleBooked(
                bookingRequestDto.getVehicleId(),
                bookingRequestDto.getFromDate(),
                bookingRequestDto.getToDate());

        if (!isAvailable) {
            throw new RuntimeException("Vehicle is not available for the selected dates");
        }
        String bookingId = UUID.randomUUID().toString();

        List<String> lockedKeys = new ArrayList<>();
        try {
            for (LocalDate date = bookingRequestDto.getFromDate();
                 !date.isAfter(bookingRequestDto.getToDate());
                 date = date.plusDays(1)) {

                String key = "vehicle:hold:" +
                        bookingRequestDto.getVehicleId() + ":" + date;

                Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
                        key,
                        bookingId,
                        15,
                        TimeUnit.MINUTES
                );

                if (Boolean.FALSE.equals(acquired)) {
                    throw new RuntimeException("Vehicle unavailable on " + date);
                }
                lockedKeys.add(key);
            }
        } catch (Exception e) {
            redisTemplate.delete(lockedKeys); // rollback
            throw e;
        }
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .userId(userId)
                .vehicleId(bookingRequestDto.getVehicleId())
                .cost(getCost(bookingRequestDto))
                .fromDate(bookingRequestDto.getFromDate())
                .endDate(bookingRequestDto.getToDate())
                .createdAt(LocalDate.now())
                .status(BookingStatus.PENDING)
                .build();
        bookingRepository.save(booking);
        return BookingResponseDto.builder()
                .BookingId(bookingId)
                .bookingStatus(BookingStatus.PENDING.name())
                .totlecost(getCost(bookingRequestDto))
                .build();
    }

    public double getCost(BookingRequestDto bookingRequestDto)
    {
        // Dummy implementation, replace with actual cost calculation logic
        long days = (bookingRequestDto.getToDate())
                .toEpochDay() - (bookingRequestDto.getFromDate()).toEpochDay() + 1;
        return days * bookingRequestDto.getCost();
    }

    public BookingResponseDto confirmBooking(String bookingId) {

        Optional<Booking> Optionalbooking =  bookingRepository.findByBookingId(bookingId);
        if(!Optionalbooking.isPresent()) {
            throw new RuntimeException("No such booking found");
        }
        Booking booking = Optionalbooking.get();

        for (LocalDate date = booking.getFromDate();
             !date.isAfter(booking.getEndDate());
             date = date.plusDays(1)) {

            String key = "vehicle:hold:" + booking.getVehicleId() + ":" + date;
            String heldBookingId = (String) redisTemplate.opsForValue().get(key);

            if (!bookingId.equals(heldBookingId)) {
                throw new RuntimeException("Booking not held for " + date);
            }
        }

        booking.setUpdatedAt(LocalDate.now());
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        for (LocalDate date = booking.getFromDate(); !date.isAfter(booking.getEndDate()); date = date.plusDays(1)) {
            redisTemplate.delete("vehicle:hold:" + booking.getVehicleId() + ":" + date);
        }

        return BookingResponseDto.builder()
                .BookingId(bookingId)
                .bookingStatus(BookingStatus.CONFIRMED.name())
                .totlecost(booking.getCost())
                .build();
    }

    public Booking cancelBooking(RequestId boookingId) {
        String bookingId = boookingId.getBookingId();
        System.out.println("Booking id recieved : " +boookingId);
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if(booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("CONFIRMED SEATES WILL NOT BE CANCELLED");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(java.time.LocalDate.now());

        return bookingRepository.save(booking);
    }
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void completeExpiredBookings()
    {
        List<Booking> bookings = bookingRepository.findByStatusAndEndDateBefore(
                BookingStatus.CONFIRMED,
                java.time.LocalDate.now()
        );
        bookings.forEach(booking -> {
            booking.setStatus(BookingStatus.COMPLETED);
            booking.setUpdatedAt(java.time.LocalDate.now());
        });
        bookingRepository.saveAll(bookings);
    }

    public List<BookingByIdResponse> getBooking(String userId) {
        List<Optional<Object>> bookings = bookingRepository.findByUserId(userId);
        List<BookingByIdResponse> response = new ArrayList<>();
        for(Optional<Object> booking : bookings)
        {
            if(booking.isPresent())
            {
                Booking b = (Booking) booking.get();
                response.add(BookingByIdResponse.builder()
                        .bookingId(b.getBookingId())
                        .vehicleId(b.getVehicleId())
                        .cost(b.getCost())
                        .fromDate(b.getFromDate())
                        .endDate(b.getEndDate())
                        .status(b.getStatus())
                        .build());
            }
        }
        return response;
    }
}
