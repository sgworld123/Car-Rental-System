package com.CarRentalSystem.PaymentService.Dto;

import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundResult {
    private String refundId;
    private PaymentStatus status;
    private double amount;
}