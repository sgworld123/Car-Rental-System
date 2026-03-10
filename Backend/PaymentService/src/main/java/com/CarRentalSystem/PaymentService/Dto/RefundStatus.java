package com.CarRentalSystem.PaymentService.Dto;

import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundStatus {
    private String bookingId;
    private PaymentStatus status;
    private double amount;
    private LocalDateTime refundedAt;
}
