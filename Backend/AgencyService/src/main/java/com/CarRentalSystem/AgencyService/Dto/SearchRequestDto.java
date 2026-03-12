package com.CarRentalSystem.AgencyService.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequestDto {
    @NotBlank(message = "Source city is required")
    private String sourceCity;

    @NotBlank(message = "Destination city is required")
    private String destinationCity;

    @NotNull(message = "From date is required")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    private LocalDate toDate;

    private int pageNumber = 0;
    private int pageSize = 10;
}
