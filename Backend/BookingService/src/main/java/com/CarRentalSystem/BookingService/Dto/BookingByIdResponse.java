package com.CarRentalSystem.BookingService.Dto;

import com.CarRentalSystem.BookingService.Models.BookingStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingByIdResponse {
    private String vehicleId;
    private double cost;
    private LocalDate fromDate;

    private LocalDate endDate;
    private BookingStatus status;

}
