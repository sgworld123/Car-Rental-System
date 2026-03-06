package com.CarRentalSystem.BookingService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailureEvent {
    private String bookingId;
    private String reason;
    private double amount;
    private LocalDateTime failedAt;
}