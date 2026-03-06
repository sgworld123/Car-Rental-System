package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.RefundResult;
import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class MockRefundProvider {

    public static RefundResult processRefund(double amount, String bookingId) {
        log.info("MockRefundProvider: processing refund of {} for booking {}", amount, bookingId);

        boolean success = Math.random() < 0.90;

        return RefundResult.builder()
                .refundId(UUID.randomUUID().toString())
                .status(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILURE)
                .amount(amount)
                .build();
    }
}