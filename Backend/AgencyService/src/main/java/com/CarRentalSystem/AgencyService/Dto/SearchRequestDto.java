package com.CarRentalSystem.AgencyService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequestDto {
    private String sourceCity;
    private String destinationCity;
    private String fromDate;
    private String toDate;
}
