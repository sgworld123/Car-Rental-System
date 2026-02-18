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
public class AgencyResponseDto {
    private String name;
    private String id;
    private String email;
    private String details;
    private String rating;
    private String agencyImage;
    private List<Reviews> reviews;
    private long phone;
    private String address;
    private String sourceCity;
    private List<Vehicle> vehicleInfo;

}
