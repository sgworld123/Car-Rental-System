package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    public void processPayment(PaymentMessageDto paymentMessageDto) {
        log.info("Processing payment for bookingId: {}, userId: {}, amount: {}, bookingStatus: {}",
                paymentMessageDto.getBookingId(),
                paymentMessageDto.getUserId(),
                paymentMessageDto.getAmount(),
                paymentMessageDto.getBookingStatus());
    }
}
