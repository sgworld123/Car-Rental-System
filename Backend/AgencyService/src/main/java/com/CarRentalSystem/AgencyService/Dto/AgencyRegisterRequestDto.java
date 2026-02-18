package com.CarRentalSystem.AgencyService.Dto;

import com.CarRentalSystem.AgencyService.Model.Reviews;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyRegisterRequestDto {
    private String name;
    private String email;
    private String details;
    private String rating;
    private List<Reviews> reviews;
    private String agencyImage;
    private long phone;
    private String address;
    private String sourceCity;
    private List<Vehicle> vehicleInfo;
}
