package com.CarRentalSystem.AgencyService.Service;

import com.CarRentalSystem.AgencyService.Dto.AgencyRegisterRequestDto;
import com.CarRentalSystem.AgencyService.Dto.AgencyResponseDto;
import com.CarRentalSystem.AgencyService.Dto.AgencySearchResponse;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.ModelType;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Repository.AgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgencyServiceTest {

    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyService agencyService;

    private Agency sampleAgency;
    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        // Workaround for the missing `final` keyword bug — inject manually
        ReflectionTestUtils.setField(agencyService, "agencyRepository", agencyRepository);

        sampleVehicle = Vehicle.builder()
                .vehicleId("Veh1234567")
                .carNumber("DL01AB1234")
                .driverName("Ramesh")
                .carModel(ModelType.SEDAN)
                .pricePerKm(12.5f)
                .description("Comfortable sedan")
                .build();

        sampleAgency = Agency.builder()
                .id("agency-001")
                .name("FastWheels")
                .email("fast@wheels.com")
                .phone(9876543210L)
                .address("12 MG Road")
                .sourceCity("Delhi")
                .rating("4.5")
                .vehicleInfo(List.of(sampleVehicle))
                .build();
    }

    // ─────────────────────────────────────────────
    // registerAgency
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("registerAgency – saves agency and returns populated DTO")
    void registerAgency_success() {
        AgencyRegisterRequestDto req = AgencyRegisterRequestDto.builder()
                .name("FastWheels")
                .email("fast@wheels.com")
                .phone(9876543210L)
                .address("12 MG Road")
                .sourceCity("Delhi")
                .rating("4.5")
                .vehicleInfo(List.of(sampleVehicle))
                .build();

        when(agencyRepository.save(any(Agency.class))).thenReturn(sampleAgency);

        AgencyResponseDto response = agencyService.registerAgency(req);

        assertThat(response.getName()).isEqualTo("FastWheels");
        assertThat(response.getEmail()).isEqualTo("fast@wheels.com");
        assertThat(response.getSourceCity()).isEqualTo("Delhi");
        verify(agencyRepository, times(1)).save(any(Agency.class));
    }

    @Test
    @DisplayName("registerAgency – vehicle list is mapped and included in response")
    void registerAgency_vehiclesMapped() {
        AgencyRegisterRequestDto req = AgencyRegisterRequestDto.builder()
                .name("SpeedCars")
                .vehicleInfo(List.of(sampleVehicle))
                .build();

        when(agencyRepository.save(any(Agency.class))).thenReturn(sampleAgency);

        AgencyResponseDto response = agencyService.registerAgency(req);

        assertThat(response.getVehicleInfo()).isNotEmpty();
    }

    // ─────────────────────────────────────────────
    // getAgenciesBySourceCity
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAgenciesBySourceCity – returns matching agencies")
    void getAgenciesBySourceCity_found() {
        when(agencyRepository.findBySourceCity("Delhi")).thenReturn(List.of(sampleAgency));

        List<AgencySearchResponse> results = agencyService.getAgenciesBySourceCity("Delhi");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("FastWheels");
        assertThat(results.get(0).getId()).isEqualTo("agency-001");
    }

    @Test
    @DisplayName("getAgenciesBySourceCity – returns empty list for unknown city")
    void getAgenciesBySourceCity_empty() {
        when(agencyRepository.findBySourceCity("Nowhere")).thenReturn(List.of());

        assertThat(agencyService.getAgenciesBySourceCity("Nowhere")).isEmpty();
    }

    // ─────────────────────────────────────────────
    // getAgencyById
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAgencyById – returns full DTO when found")
    void getAgencyById_found() {
        when(agencyRepository.findById("agency-001")).thenReturn(Optional.of(sampleAgency));

        AgencyResponseDto response = agencyService.getAgencyById("agency-001");

        assertThat(response.getName()).isEqualTo("FastWheels");
        assertThat(response.getId()).isEqualTo("agency-001");
    }

    @Test
    @DisplayName("getAgencyById – throws RuntimeException when not found")
    void getAgencyById_notFound_throws() {
        when(agencyRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agencyService.getAgencyById("bad-id"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Agency not found with id: bad-id");
    }

    // ─────────────────────────────────────────────
    // isAgencyValid
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("isAgencyValid – returns true when agency exists")
    void isAgencyValid_exists() {
        when(agencyRepository.existsById("agency-001")).thenReturn(true);
        assertThat(agencyService.isAgencyValid("agency-001")).isTrue();
    }

    @Test
    @DisplayName("isAgencyValid – returns false when agency doesn't exist")
    void isAgencyValid_notExists() {
        when(agencyRepository.existsById("ghost")).thenReturn(false);
        assertThat(agencyService.isAgencyValid("ghost")).isFalse();
    }

    // ─────────────────────────────────────────────
    // isVehicleValid — BUG: still uses findAll() (full table scan)
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("isVehicleValid – returns true when vehicle exists in an agency")
    void isVehicleValid_found() {
        when(agencyRepository.findAll()).thenReturn(List.of(sampleAgency));

        assertThat(agencyService.isVehicleValid("Veh1234567")).isTrue();
    }

    @Test
    @DisplayName("isVehicleValid – returns false for unknown vehicleId")
    void isVehicleValid_notFound() {
        when(agencyRepository.findAll()).thenReturn(List.of(sampleAgency));

        assertThat(agencyService.isVehicleValid("ghost-veh")).isFalse();
    }

    @Test
    @DisplayName("[BUG] isVehicleValid – calls findAll() which is an O(n) full scan; fix with targeted query")
    void isVehicleValid_fullTableScan_documented() {
        when(agencyRepository.findAll()).thenReturn(List.of(sampleAgency));

        agencyService.isVehicleValid("Veh1234567");

        // This verifies the bug is present and should be replaced with:
        // agencyRepository.existsByVehicleInfo_VehicleId(vehicleId)
        verify(agencyRepository, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getVehicleById
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getVehicleById – returns correct vehicle by ID")
    void getVehicleById_found() {
        when(agencyRepository.findAll()).thenReturn(List.of(sampleAgency));

        Vehicle result = agencyService.getVehicleById("Veh1234567");

        assertThat(result.getCarNumber()).isEqualTo("DL01AB1234");
        assertThat(result.getDriverName()).isEqualTo("Ramesh");
    }

    @Test
    @DisplayName("getVehicleById – throws when vehicle not found")
    void getVehicleById_notFound_throws() {
        when(agencyRepository.findAll()).thenReturn(List.of(sampleAgency));

        assertThatThrownBy(() -> agencyService.getVehicleById("ghost-veh"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vehicle not found with id: ghost-veh");
    }
}