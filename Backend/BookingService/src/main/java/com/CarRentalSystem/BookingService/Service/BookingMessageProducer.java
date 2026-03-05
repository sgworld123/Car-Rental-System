package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Dto.PaymentMessageDto;
import com.CarRentalSystem.BookingService.Models.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendBookingMessage(Booking booking) {
        PaymentMessageDto paymentMessageDto = PaymentMessageDto.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .amount(booking.getCost())
                .bookingStatus(booking.getStatus())
                .build();
        rabbitTemplate.convertAndSend("bookingQueue", paymentMessageDto);
    }
}
