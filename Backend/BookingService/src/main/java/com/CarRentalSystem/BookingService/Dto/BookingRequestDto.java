package com.CarRentalSystem.BookingService.Dto;

import com.CarRentalSystem.BookingService.Utils.SecurityUtils;
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
    private String vehicleId;
    private int cost;
    private LocalDate fromDate;
    private LocalDate toDate;
}
