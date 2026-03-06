package com.CarRentalSystem.BookingService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {
    private String bookingId;
    private String userId;
    private String vehicleId;
    private double amount;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDateTime createdAt;
}