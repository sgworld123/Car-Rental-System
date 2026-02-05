package com.CarRentalSystem.BookingService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseDto {
    private String BookingId;
    private String bookingStatus;
    private double totlecost;
}
