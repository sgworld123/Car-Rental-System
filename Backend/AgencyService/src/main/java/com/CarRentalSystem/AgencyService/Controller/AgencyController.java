package com.CarRentalSystem.AgencyService.Controller;

import com.CarRentalSystem.AgencyService.Dto.AgencySearchResponse;
import com.CarRentalSystem.AgencyService.Dto.AgencyRegisterRequestDto;
import com.CarRentalSystem.AgencyService.Dto.AgencyResponseDto;
import com.CarRentalSystem.AgencyService.Dto.SearchRequestDto;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@RequiredArgsConstructor
public class AgencyController {
    private final AgencyService agencyService;

    @PostMapping("/register")
    public AgencyResponseDto registerAgency(@RequestBody AgencyRegisterRequestDto agencyRegisterRequestDto)
    {
        return agencyService.registerAgency(agencyRegisterRequestDto);
    }
    @PostMapping("/search")
    public List<AgencySearchResponse> searchAgencies(@RequestBody SearchRequestDto searchRequestDto) {
        return agencyService.getAgenciesBySourceCity(searchRequestDto.getSourceCity());
    }
    @GetMapping("/{agencyId}")
    public AgencyResponseDto getAgencyById(@PathVariable String agencyId)
    {
        return agencyService.getAgencyById(agencyId);
    }
    @GetMapping("/validate/{agencyId}")
    public boolean validateAgency(@PathVariable String agencyId)
    {
        return agencyService.isAgencyValid(agencyId);
    }
    @GetMapping("/vehicle/validate/{vehicleId}")
    public boolean validateVehicle(@PathVariable String vehicleId)
    {
        return agencyService.isVehicleValid(vehicleId);
    }
    @GetMapping("/vehicle/{vehicleId}")
    public Vehicle getVehicleById(@PathVariable String vehicleId)
    {
        return agencyService.getVehicleById(vehicleId);
    }
}
