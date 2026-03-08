package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Config.PaymentRabbitMQConfig;
import com.CarRentalSystem.BookingService.Dto.PaymentFailureEvent;
import com.CarRentalSystem.BookingService.Dto.PaymentSuccessEvent;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final BookingRepository bookingRepository;
    private final RedisTemplate<String,String> redisTemplate;
    @RabbitListener(queues = PaymentRabbitMQConfig.PAYMENT_SUCCESS_QUEUE)
    public void handlePaymentSuccess(PaymentSuccessEvent paymentSuccessEvent)
    {
        log.info("Received PaymentSuccessEvent for bookingId: {}", paymentSuccessEvent.getBookingId());
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(paymentSuccessEvent.getBookingId());
        if(optionalBooking.isPresent())
        {
            Booking booking = optionalBooking.get();
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);
        }
        else
        {
            log.error("No booking found for payment success event with bookingId: " + paymentSuccessEvent.getBookingId());
        }
    }
    @RabbitListener(queues = PaymentRabbitMQConfig.PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailure(PaymentFailureEvent paymentFailureEvent)
    {
        log.info("Received PaymentFailedEvent for bookingId: {}", paymentFailureEvent.getBookingId());
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(paymentFailureEvent.getBookingId());
        if(optionalBooking.isPresent())
        {
            Booking booking = optionalBooking.get();
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);
            for (LocalDate date = booking.getFromDate(); !date.isAfter(booking.getEndDate()); date = date.plusDays(1)) {
                redisTemplate.delete( booking.getVehicleId() + ":" + date);
            }
        }
        else
        {
            log.error("No booking found for payment success event with bookingId: " + paymentFailureEvent.getBookingId());
        }
    }
}
