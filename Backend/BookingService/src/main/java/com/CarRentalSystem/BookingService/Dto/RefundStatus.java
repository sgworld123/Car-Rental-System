package com.CarRentalSystem.BookingService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
