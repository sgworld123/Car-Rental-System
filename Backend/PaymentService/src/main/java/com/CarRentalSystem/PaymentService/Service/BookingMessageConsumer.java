package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import com.CarRentalSystem.PaymentService.Dto.PaymentResult;
import com.CarRentalSystem.PaymentService.Models.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingMessageConsumer {

    private final PaymentService paymentService;

    @RabbitListener(queues = "bookingQueue")
    public Payment consumeBookingMessage(PaymentMessageDto paymentMessageDto) {
        return paymentService.processPayment(paymentMessageDto);
    }
}
