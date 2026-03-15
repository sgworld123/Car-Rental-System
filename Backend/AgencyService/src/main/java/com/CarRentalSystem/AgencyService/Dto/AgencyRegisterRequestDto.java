package com.CarRentalSystem.AgencyService.Dto;

import com.CarRentalSystem.AgencyService.Model.Reviews;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Agency name cannot be empty")
    @Size(min = 3, max = 100, message = "Agency name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Details cannot be empty")
    @Size(max = 500, message = "Details cannot exceed 500 characters")
    private String details;

    @Pattern(
            regexp = "^[0-5](\\.[0-9])?$",
            message = "Rating must be between 0 and 5"
    )
    private String rating;

    @Valid
    private List<Reviews> reviews;

    @NotBlank(message = "Agency image cannot be empty")
    private String agencyImage;

    @Min(value = 1000000000L, message = "Phone number must be valid")
    @Max(value = 9999999999L, message = "Phone number must be 10 digits")
    private long phone;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Source city cannot be empty")
    private String sourceCity;

    @Valid
    @NotEmpty(message = "Vehicle information cannot be empty")
    private List<Vehicle> vehicleInfo;
}