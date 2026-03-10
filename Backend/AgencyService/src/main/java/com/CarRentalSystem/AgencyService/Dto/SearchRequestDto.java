package com.CarRentalSystem.AgencyService.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequestDto {
    @NotBlank
    private String sourceCity;
    @NotBlank
    private String destinationCity;
    @NotNull
    private String fromDate;
    @NotNull
    private String toDate;
    private int pageNumber = 0;
    private int pageSize = 10;
}
