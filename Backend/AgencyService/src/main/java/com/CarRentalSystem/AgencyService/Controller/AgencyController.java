package com.CarRentalSystem.AgencyService.Controller;

import com.CarRentalSystem.AgencyService.Dto.*;
import com.CarRentalSystem.AgencyService.Exceptions.VehicleNotFoundException;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Service.AgencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@RequiredArgsConstructor
public class AgencyController {
    private final AgencyService agencyService;

    @PostMapping("/register")
    public ResponseEntity<AgencyResponseDto> registerAgency(@Valid @RequestBody AgencyRegisterRequestDto agencyRegisterRequestDto)
    {
        return ResponseEntity.ok(agencyService.registerAgency(agencyRegisterRequestDto));
    }
    @PostMapping("/search")
    public ResponseEntity<PagedSearchResponse> searchAgenciesBySourceCity(@Valid @RequestBody SearchRequestDto searchRequestDto) {
        return ResponseEntity.ok(agencyService.getAgenciesBySourceCity(searchRequestDto));
    }
    @GetMapping("/{agencyId}")
    public ResponseEntity<AgencyResponseDto> getAgencyById(@PathVariable String agencyId)
    {
        return ResponseEntity.ok(agencyService.getAgencyById(agencyId));
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
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String vehicleId) throws VehicleNotFoundException {
        return ResponseEntity.ok(agencyService.getVehicleById(vehicleId));
    }
}
