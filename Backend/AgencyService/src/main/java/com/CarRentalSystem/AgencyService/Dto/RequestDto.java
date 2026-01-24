package com.CarRentalSystem.AgencyService.Dto;

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
public class RequestDto {
    private String name;
    private String email;
    private long phone;
    private List<String> cities;
    private String password;
    private List<Vehicle> vehicleInfo;
}
