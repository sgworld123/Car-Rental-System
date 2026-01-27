package com.CarRentalSystem.BookingService.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    private String agencyId;
    private String vehicleId;
    private String fromDate;
    private String toDate;

}
