package com.CarRentalSystem.BookingService.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @Positive(message = "Cost must be positive")
    private int cost;

    @NotNull(message = "From date is required")
    @FutureOrPresent(message = "From date cannot be in the past")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    @Future(message = "To date must be in the future")
    private LocalDate toDate;

    private String imageUrl;
    private String carName;
}