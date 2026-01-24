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
public class ResponseDto {
    private String name;
    private String email;
    private long phone;

}
