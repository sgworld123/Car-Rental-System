package com.CarRentalSystem.PaymentService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancelledEvent {
    private String transactionId;
    private String vehicleId;
    private double amount;
    private LocalDateTime cancelledAt;
}
