package com.CarRentalSystem.AgencyService.Service;

import com.CarRentalSystem.AgencyService.Dto.*;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Repository.AgencyRepository;
import com.CarRentalSystem.AgencyService.Repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyService {
    private final AgencyRepository agencyRepository;
    private final VehicleRepository vehicleRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    //register agency
    public AgencyResponseDto registerAgency(AgencyRegisterRequestDto agencyRegisterRequestDto)
    {
        Agency agency = Agency.builder()
                .name(agencyRegisterRequestDto.getName())
                .address(agencyRegisterRequestDto.getAddress())
                .details(agencyRegisterRequestDto.getDetails())
                .rating(agencyRegisterRequestDto.getRating())
                .agencyImage(agencyRegisterRequestDto.getAgencyImage())
                .reviews(agencyRegisterRequestDto.getReviews())
                .sourceCity(agencyRegisterRequestDto.getSourceCity())
                .email(agencyRegisterRequestDto.getEmail())
                .phone(agencyRegisterRequestDto.getPhone())
                .build();

        List<Vehicle> vehicles = agencyRegisterRequestDto.getVehicleInfo().stream()
                .map(v -> Vehicle.builder()
                        .vehicleId("Veh" + (int)(Math.random() * 10000000))
                        .carModel(v.getCarModel())
                        .pricePerKm(v.getPricePerKm())
                        .carNumber(v.getCarNumber())
                        .description(v.getDescription())
                        .driverName(v.getDriverName())
                        .coverImage(v.getCoverImage())
                        .images(v.getImages())
                        .reviews(v.getReviews())
                        .build())
                .toList();
        vehicleRepository.saveAll(vehicles);
        agency.setVehicleInfo(vehicles);

        Agency savedAgency = agencyRepository.save(agency);

        return AgencyResponseDto.builder()
                .email(savedAgency.getEmail())
                .name(savedAgency.getName())
                .phone((savedAgency.getPhone()))
                .agencyImage(savedAgency.getAgencyImage())
                .reviews(savedAgency.getReviews())
                .address(savedAgency.getAddress())
                .rating(savedAgency.getRating())
                .sourceCity(savedAgency.getSourceCity())
                .vehicleInfo(savedAgency.getVehicleInfo())
                .details(savedAgency.getDetails())
                .build();
    }

    public PagedSearchResponse getAgenciesBySourceCity(SearchRequestDto searchRequestDto) {
        String key = searchRequestDto.getSourceCity() + "_" + searchRequestDto.getPageNumber();
        PagedSearchResponse cachedResponse = (PagedSearchResponse) redisTemplate.opsForValue().get(key);
        if (cachedResponse != null) {
            return cachedResponse;
        }
        Pageable pageable = PageRequest.of(searchRequestDto.getPageNumber(), searchRequestDto.getPageSize());
        Page<Agency> pageAgency = agencyRepository.findBySourceCity(searchRequestDto.getSourceCity(),pageable);
        List<Agency> agencies = pageAgency.getContent();
        List<AgencySearchResponse> agencyList = agencies.stream().map(agency -> AgencySearchResponse.builder()
                .id(agency.getId())
                .name(agency.getName())
                .email(agency.getEmail())
                .agencyImage(agency.getAgencyImage())
                .rating(agency.getRating())
                .address(agency.getAddress())
                .phone(agency.getPhone())
                .build()).toList();
        PagedSearchResponse pagedSearchResponse = PagedSearchResponse.builder()
                .agencies(agencyList)
                .currentPage(pageAgency.getNumber())
                .totalPages(pageAgency.getTotalPages())
                .totleItems((int) pageAgency.getTotalElements())
                .pageSize(pageAgency.getSize())
                .build();
        redisTemplate.opsForValue().set(searchRequestDto.getSourceCity() + "_" + searchRequestDto.getPageNumber(),pagedSearchResponse, Duration.ofMinutes(10));
        return pagedSearchResponse;
    }
    public AgencyResponseDto getAgencyById(String agencyId) {
        return agencyRepository.findById(agencyId)
                .map(agency -> AgencyResponseDto.builder()
                        .name(agency.getName())
                        .id(agency.getId())
                        .email(agency.getEmail())
                        .phone(agency.getPhone())
                        .details(agency.getDetails())
                        .agencyImage(agency.getAgencyImage())
                        .rating(agency.getRating())
                        .reviews(agency.getReviews())
                        .address(agency.getAddress())
                        .sourceCity(agency.getSourceCity())
                        .vehicleInfo(agency.getVehicleInfo())
                        .build())
                .orElseThrow(() -> new RuntimeException("Agency not found with id: " + agencyId));
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

    public Vehicle getVehicleById(String vehicleId) {
        List<Agency> agencies = agencyRepository.findAll();
        for (Agency agency : agencies) {
            for (Vehicle vehicle : agency.getVehicleInfo()) {
                if (vehicle.getVehicleId().equals(vehicleId)) {
                    return vehicle;
                }
            }
        }
        throw new RuntimeException("Vehicle not found with id: " + vehicleId);
    }
}
