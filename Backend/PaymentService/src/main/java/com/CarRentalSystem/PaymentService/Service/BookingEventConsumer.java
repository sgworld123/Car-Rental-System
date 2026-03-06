package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Configs.BookingRabbitMQConfig;
import com.CarRentalSystem.PaymentService.Dto.BookingCreatedEvent;
import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingEventConsumer {
    private final PaymentService paymentService;
    @RabbitListener(queues = BookingRabbitMQConfig.BOOKING_CREATED_QUEUE)
    public void consumePaymentMessage(BookingCreatedEvent paymentMessageDto) {
        log.info("Received payment message for bookingId: {}", paymentMessageDto.getBookingId());
        paymentService.processPayment(PaymentMessageDto.builder()
                .bookingId(paymentMessageDto.getBookingId())
                .userId(paymentMessageDto.getUserId())
                .amount(paymentMessageDto.getAmount())
                .build());
    }
    @RabbitListener(queues = BookingRabbitMQConfig.BOOKING_CANCELLED_QUEUE)
    public void consumePaymentFailedMessage(BookingCreatedEvent paymentMessageDto) {
        log.info("Received payment failed message for bookingId: {}", paymentMessageDto.getBookingId());
        // Handle payment failure logic here, e.g., notify user, retry, etc.
    }
}
