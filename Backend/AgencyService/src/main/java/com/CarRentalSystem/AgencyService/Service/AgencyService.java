package com.CarRentalSystem.AgencyService.Service;

import com.CarRentalSystem.AgencyService.Dto.AgencySearchResponse;
import com.CarRentalSystem.AgencyService.Dto.AgencyRegisterRequestDto;
import com.CarRentalSystem.AgencyService.Dto.AgencyResponseDto;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Repository.AgencyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;

    //register agency
    public AgencyResponseDto registerAgency(AgencyRegisterRequestDto agencyRegisterRequestDto)
    {
        Agency agency = Agency.builder()
                .name(agencyRegisterRequestDto.getName())
                .address(agencyRegisterRequestDto.getAddress())
                .sourceCity(agencyRegisterRequestDto.getSourceCity())
                .email(agencyRegisterRequestDto.getEmail())
                .phone(agencyRegisterRequestDto.getPhone())
                .build();

        List<Vehicle> vehicles = agencyRegisterRequestDto.getVehicleInfo().stream()
                .map(v -> Vehicle.builder()
                        .vehicleId("Veh" + (int)(Math.random() * 10000000))
                        .carModel(v.getCarModel())
                        .pricePerKm(v.getPricePerKm())
                        .rating(v.getRating())
                        .build())
                .toList();
        agency.setVehicleInfo(vehicles);

        Agency savedAgency = agencyRepository.save(agency);
        System.out.println(savedAgency.getId());

        return AgencyResponseDto.builder()
                .email(savedAgency.getEmail())
                .name(savedAgency.getName())
                .phone((savedAgency.getPhone()))
                .build();
    }

    //get agencies with source city
    public List<AgencySearchResponse> getAgenciesBySourceCity(String sourceCity) {
        List<Agency> agencies = agencyRepository.findBySourceCity(sourceCity);
        return agencies.stream().map(agency -> AgencySearchResponse.builder()
                .name(agency.getName())
                .email(agency.getEmail())
                .address(agency.getAddress())
                .phone(agency.getPhone())
                .build()).toList();
    }

    public AgencyResponseDto getAgencyById(String agencyId) {
        return agencyRepository.findById(agencyId)
                .map(agency -> AgencyResponseDto.builder()
                        .name(agency.getName())
                        .email(agency.getEmail())
                        .phone(agency.getPhone())
                        .build())
                .orElseThrow(() -> new RuntimeException("Agency not found with id: " + agencyId));
    }
    public List<Vehicle> getVehiclesByAgencyId(String agencyId) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency not found with id: " + agencyId));
        return agency.getVehicleInfo();
    }

    public boolean isAgencyValid(String agencyId) {
        return agencyRepository.existsById(agencyId);
    }

    public boolean isVehicleValid(String vehicleId) {
        List<Agency> agencies = agencyRepository.findAll();
        for (Agency agency : agencies) {
            for (Vehicle vehicle : agency.getVehicleInfo()) {
                if (vehicle.getVehicleId().equals(vehicleId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
