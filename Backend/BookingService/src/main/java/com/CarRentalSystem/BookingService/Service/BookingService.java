package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Dto.*;
import com.CarRentalSystem.BookingService.Exceptions.*;
import com.CarRentalSystem.BookingService.Models.BookedVehicleAndDates;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Repository.BookedVehicleAndDatesRepository;
import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookedVehicleAndDatesRepository bookedVehicleAndDatesRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final BookingEventPublisher bookingEventPublisher;
    @Transactional
    public BookingResponseDto createBooking(String userId ,BookingRequestDto bookingRequestDto)
    {
        String bookingId = UUID.randomUUID().toString();

        List<String> lockedKeys = new ArrayList<>();
        try {
            for (LocalDate date = bookingRequestDto.getFromDate();
                 !date.isAfter(bookingRequestDto.getToDate());
                 date = date.plusDays(1)) {

                String key =
                        bookingRequestDto.getVehicleId() + ":" + date;

                Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
                        key,
                        bookingId,
                        Duration.ofMinutes(15)
                );
                if (Boolean.FALSE.equals(acquired)) {
                    throw new VehicleUnavailableOnDatesException("Vehicle unavailable on " + date);
                }
                BookedVehicleAndDates bookedVehicleAndDates = BookedVehicleAndDates.builder()
                        .bookingId(bookingId)
                        .vehicleId(bookingRequestDto.getVehicleId())
                        .date(date)
                        .build();
                bookedVehicleAndDatesRepository.save(bookedVehicleAndDates);
                lockedKeys.add(key);
            }
        } catch (Exception e) {
            redisTemplate.delete(lockedKeys);
            removeFromBookedVehicleAndDates(bookingId);
            throw e;
        }

        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .userId(userId)
                .vehicleId(bookingRequestDto.getVehicleId())
                .cost(bookingRequestDto.getCost())
                .fromDate(bookingRequestDto.getFromDate())
                .endDate(bookingRequestDto.getToDate())
                .createdAt(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .build();
        bookingRepository.save(booking);
        return BookingResponseDto.builder()
                .bookingId(bookingId)
                .bookingStatus(BookingStatus.PENDING.name())
                .totalCost(booking.getCost())
                .build();
    }
    private void removeFromBookedVehicleAndDates(String bookingId) {
        bookedVehicleAndDatesRepository.deleteByBookingId(bookingId);
    }
    public double getCost(BookingRequestDto bookingRequestDto)
    {
        long days = (bookingRequestDto.getToDate())
                .toEpochDay() - (bookingRequestDto.getFromDate()).toEpochDay() + 1;
        return days * bookingRequestDto.getCost();
    }

    public BookingResponseDto confirmBooking(String userId,String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with Id : " + bookingId));
        if(!Objects.equals(booking.getUserId(), userId))
        {
            throw new UserIsUnauthorizedException("Unauthorized confirmation attempt");
        }
        if(booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("BOOKING ALREADY CONFIRMED");
        } else if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("CANCELLED BOOKING CANNOT BE CONFIRMED");
        } else if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingAlreadyCompletedException("COMPLETED BOOKING CANNOT BE CONFIRMED");
        }
        else {
            bookingEventPublisher.handleBookingCreated(BookingCreatedEvent.builder()
                    .bookingId(booking.getBookingId())
                    .userId(booking.getUserId())
                    .amount(booking.getCost())
                    .build());
            return BookingResponseDto.builder()
                    .bookingId(booking.getBookingId())
                    .bookingStatus(BookingStatus.PENDING.name())
                    .totalCost(booking.getCost())
                    .build();
        }
    }
    public BookingResponseDto cancelBooking(String userId, RequestId boookingId) {
        String bookingId = boookingId.getBookingId();
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        if(!Objects.equals(booking.getUserId(), userId))
        {
            throw new UserIsUnauthorizedException("Unauthorized cancellation attempt");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingIsAlreadyCancelledException("BOOKING ALREADY CANCELLED");
        }
        if(booking.getStatus() == BookingStatus.PENDING) {
            throw new RuntimeException("PENDING BOOKING CANNOT BE CANCELLED");
        }
        if(booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingAlreadyCompletedException("BOOKING ALREADY COMPLETED");
        }
        bookedVehicleAndDatesRepository.deleteByBookingId(bookingId);
        bookingEventPublisher.handleBookingCancelled(BookingCancelledEvent.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .amount(booking.getCost())
                .build());
        return BookingResponseDto.builder()
                .bookingId(booking.getBookingId())
                .bookingStatus(BookingStatus.CANCELLED.name())
                .totalCost(booking.getCost())
                .build();
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
            booking.setUpdatedAt(LocalDateTime.now());
            bookedVehicleAndDatesRepository.deleteByBookingId(booking.getBookingId());

        });
        bookingRepository.saveAll(bookings);
    }

    public List<BookingByIdResponse> getBooking(String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        List<BookingByIdResponse> response = new ArrayList<>();
        for(Booking b : bookings)
        {
            response.add(BookingByIdResponse.builder()
                    .bookingId(b.getBookingId())
                    .vehicleId(b.getVehicleId())
                    .cost(b.getCost())
                    .fromDate(b.getFromDate())
                    .endDate(b.getEndDate())
                    .status(b.getStatus())
                    .build());
        }
        return response;
    }
}
