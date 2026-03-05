package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentResult;
import com.CarRentalSystem.PaymentService.Errors.Exceptions;
import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MockPaymentProvider {
    public static PaymentResult processPayment(double amount) {
        int random = (int) (Math.random() * 100);
        if (random < 80) {
            return PaymentResult.builder()
                    .status(PaymentStatus.SUCCESS)
                    .transactionId(UUID.randomUUID().toString())
                    .failureReason(null)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        else if (random < 95)
        {
            return PaymentResult.builder()
                    .status(PaymentStatus.FAILURE)
                    .transactionId(UUID.randomUUID().toString())
                    .failureReason("Insufficient funds")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        else
        {
            throw new Exceptions.PaymentProcessingException("Payment gateway timeout");
        }
    }
}
