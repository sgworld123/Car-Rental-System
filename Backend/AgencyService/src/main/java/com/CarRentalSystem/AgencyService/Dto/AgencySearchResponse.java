package com.CarRentalSystem.AgencyService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencySearchResponse {
    private String name;
    private String email;
    private String address;
    private long phone;
}
