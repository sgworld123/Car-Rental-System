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
public class PaymentSuccessEvent {
    private String bookingId;
    private String transactionId;
    private double amount;
    private LocalDateTime paidAt;
}