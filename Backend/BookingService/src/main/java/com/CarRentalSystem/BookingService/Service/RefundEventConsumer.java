package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Config.PaymentRabbitMQConfig;
import com.CarRentalSystem.BookingService.Dto.PaymentStatus;
import com.CarRentalSystem.BookingService.Dto.RefundStatus;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundEventConsumer {
    private final BookingRepository bookingRepository;
    @RabbitListener(queues = PaymentRabbitMQConfig.PAYMENT_REFUND_QUEUE)
    public void consumeRefundResult(RefundStatus refundStatus) {
        if (refundStatus.getStatus() == PaymentStatus.REFUND_SUCCESS) {
            Booking booking = bookingRepository.findByBookingId(refundStatus.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Booking not found for refund result with bookingId: " + refundStatus.getBookingId()));
            booking.setStatus(BookingStatus.REFUNDED);
            bookingRepository.save(booking);
        } else {
            log.error("Refund failed for bookingId: {}. Status: {}", refundStatus.getBookingId(), refundStatus.getStatus());
        }
    }
}
