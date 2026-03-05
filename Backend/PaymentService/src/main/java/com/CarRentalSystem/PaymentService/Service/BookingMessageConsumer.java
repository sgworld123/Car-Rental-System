package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingMessageConsumer {

    private final PaymentService paymentService;

    @RabbitListener(queues = "bookingQueue")
    public void consumeBookingMessage(PaymentMessageDto paymentMessageDto) {
        paymentService.processPayment(paymentMessageDto);
    }
}
