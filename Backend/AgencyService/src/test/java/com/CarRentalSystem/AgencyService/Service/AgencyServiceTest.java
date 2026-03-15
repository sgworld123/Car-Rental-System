package com.CarRentalSystem.AgencyService.Service;

import com.CarRentalSystem.AgencyService.Dto.*;
import com.CarRentalSystem.AgencyService.Exceptions.AgencyNotFoundException;
import com.CarRentalSystem.AgencyService.Exceptions.InvalidDatesException;
import com.CarRentalSystem.AgencyService.Exceptions.VehicleNotFoundException;
import com.CarRentalSystem.AgencyService.Model.Agency;
import com.CarRentalSystem.AgencyService.Model.Vehicle;
import com.CarRentalSystem.AgencyService.Repository.AgencyRepository;
import com.CarRentalSystem.AgencyService.Repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AgencyServiceTest {

    @Mock private AgencyRepository agencyRepository;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;

    @InjectMocks private AgencyService agencyService;

    private static final LocalDate FUTURE_FROM = LocalDate.now().plusDays(2);
    private static final LocalDate FUTURE_TO   = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        // openMocks handles generic types correctly unlike @ExtendWith(MockitoExtension.class)
        MockitoAnnotations.openMocks(this);
    }

    // ─────────────────────────────────────────────
    // registerAgency
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("registerAgency – saves vehicles and agency, returns correct DTO")
    void registerAgency_success() {
        Vehicle v = Vehicle.builder().carNumber("KA01AB1234").build();
        AgencyRegisterRequestDto req = AgencyRegisterRequestDto.builder()
                .name("SpeedRent")
                .email("speed@rent.com")
                .address("MG Road, Bangalore")
                .sourceCity("Bangalore")
                .rating("4.5")
                .phone(9876543210L)
                .vehicleInfo(List.of(v))
                .build();

        Agency saved = Agency.builder()
                .id("agency-1")
                .name("SpeedRent")
                .email("speed@rent.com")
                .address("MG Road, Bangalore")
                .sourceCity("Bangalore")
                .rating("4.5")
                .phone(9876543210L)
                .vehicleInfo(List.of(v))
                .build();

        when(agencyRepository.save(any(Agency.class))).thenReturn(saved);

        AgencyResponseDto result = agencyService.registerAgency(req);

        assertThat(result.getId()).isEqualTo("agency-1");
        assertThat(result.getName()).isEqualTo("SpeedRent");
        assertThat(result.getSourceCity()).isEqualTo("Bangalore");
        verify(vehicleRepository).saveAll(anyList());
        verify(agencyRepository).save(any(Agency.class));
    }

    @Test
    @DisplayName("registerAgency – saves all vehicles passed in the request")
    void registerAgency_savesAllVehicles() {
        List<Vehicle> vehicles = List.of(
                Vehicle.builder().carNumber("KA01AB1111").build(),
                Vehicle.builder().carNumber("KA01AB2222").build(),
                Vehicle.builder().carNumber("KA01AB3333").build()
        );

        AgencyRegisterRequestDto req = AgencyRegisterRequestDto.builder()
                .name("MultiCar").email("multi@car.com")
                .vehicleInfo(vehicles)
                .build();

        when(agencyRepository.save(any())).thenReturn(
                Agency.builder().id("a1").vehicleInfo(vehicles).build()
        );

        agencyService.registerAgency(req);

        verify(vehicleRepository).saveAll(argThat(list -> ((List<?>) list).size() == 3));
    }

    // ─────────────────────────────────────────────
    // getAgenciesBySourceCity
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAgenciesBySourceCity – returns cached response without hitting DB")
    void getAgenciesBySourceCity_cacheHit_returnsCachedResponse() {
        SearchRequestDto req = buildSearchRequest("Bangalore");
        PagedSearchResponse cached = PagedSearchResponse.builder()
                .agencies(List.of())
                .currentPage(0).totalPages(1).totalItems(0).pageSize(10)
                .build();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("Bangalore_0")).thenReturn(cached);

        PagedSearchResponse result = agencyService.getAgenciesBySourceCity(req);

        assertThat(result).isEqualTo(cached);
        verify(agencyRepository, never()).findBySourceCity(any(), any());
    }

    @Test
    @DisplayName("getAgenciesBySourceCity – cache miss hits DB and caches result")
    void getAgenciesBySourceCity_cacheMiss_queriesDbAndCaches() {
        SearchRequestDto req = buildSearchRequest("Mumbai");

        Agency agency = Agency.builder()
                .id("a1").name("MumbaiRent").email("m@r.com")
                .address("Andheri").phone(9999999999L).rating("4.0")
                .sourceCity("Mumbai")
                .build();

        Page<Agency> page = new PageImpl<>(List.of(agency));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("Mumbai_0")).thenReturn(null);
        when(agencyRepository.findBySourceCity(eq("Mumbai"), any(Pageable.class))).thenReturn(page);

        PagedSearchResponse result = agencyService.getAgenciesBySourceCity(req);

        assertThat(result.getAgencies()).hasSize(1);
        assertThat(result.getAgencies().get(0).getName()).isEqualTo("MumbaiRent");
        verify(valueOperations).set(eq("Mumbai_0"), any(), any());
    }

    @Test
    @DisplayName("getAgenciesBySourceCity – throws InvalidDatesException for past fromDate")
    void getAgenciesBySourceCity_pastFromDate_throws() {
        SearchRequestDto req = SearchRequestDto.builder()
                .sourceCity("Delhi")
                .fromDate(LocalDate.now().minusDays(1))
                .toDate(FUTURE_TO)
                .pageNumber(0).pageSize(10)
                .build();

        assertThatThrownBy(() -> agencyService.getAgenciesBySourceCity(req))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessageContaining("future");
    }

    @Test
    @DisplayName("getAgenciesBySourceCity – throws InvalidDatesException for past toDate")
    void getAgenciesBySourceCity_pastToDate_throws() {
        SearchRequestDto req = SearchRequestDto.builder()
                .sourceCity("Delhi")
                .fromDate(FUTURE_FROM)
                .toDate(LocalDate.now().minusDays(1))
                .pageNumber(0).pageSize(10)
                .build();

        assertThatThrownBy(() -> agencyService.getAgenciesBySourceCity(req))
                .isInstanceOf(InvalidDatesException.class);
    }

    @Test
    @DisplayName("getAgenciesBySourceCity – throws AgencyNotFoundException when no agencies in city")
    void getAgenciesBySourceCity_noAgencies_throws() {
        SearchRequestDto req = buildSearchRequest("UnknownCity");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("UnknownCity_0")).thenReturn(null);
        when(agencyRepository.findBySourceCity(eq("UnknownCity"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> agencyService.getAgenciesBySourceCity(req))
                .isInstanceOf(AgencyNotFoundException.class)
                .hasMessageContaining("No agencies found");
    }

    // ─────────────────────────────────────────────
    // getAgencyById
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAgencyById – returns mapped DTO for existing agency")
    void getAgencyById_found_returnsMappedDto() {
        Agency agency = Agency.builder()
                .id("agency-99").name("TopRent").email("top@rent.com")
                .phone(1234567890L).sourceCity("Chennai")
                .build();

        when(agencyRepository.findById("agency-99")).thenReturn(Optional.of(agency));

        AgencyResponseDto result = agencyService.getAgencyById("agency-99");

        assertThat(result.getId()).isEqualTo("agency-99");
        assertThat(result.getName()).isEqualTo("TopRent");
        assertThat(result.getSourceCity()).isEqualTo("Chennai");
    }

    @Test
    @DisplayName("getAgencyById – throws AgencyNotFoundException for unknown id")
    void getAgencyById_notFound_throws() {
        when(agencyRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agencyService.getAgencyById("bad-id"))
                .isInstanceOf(AgencyNotFoundException.class)
                .hasMessageContaining("bad-id");
    }

    // ─────────────────────────────────────────────
    // getVehicleById
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getVehicleById – returns vehicle for valid vehicleId")
    void getVehicleById_found_returnsVehicle() {
        Vehicle vehicle = Vehicle.builder().vehicleId("veh-1").carNumber("KA01AB0001").build();
        when(vehicleRepository.findByVehicleId("veh-1")).thenReturn(vehicle);

        Vehicle result = agencyService.getVehicleById("veh-1");

        assertThat(result.getVehicleId()).isEqualTo("veh-1");
        assertThat(result.getCarNumber()).isEqualTo("KA01AB0001");
    }

    @Test
    @DisplayName("getVehicleById – throws VehicleNotFoundException for unknown vehicleId")
    void getVehicleById_notFound_throws() {
        when(vehicleRepository.findByVehicleId("ghost")).thenReturn(null);

        assertThatThrownBy(() -> agencyService.getVehicleById("ghost"))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining("ghost");
    }

    // ─────────────────────────────────────────────
    // isAgencyValid / isVehicleValid
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("isAgencyValid – returns true when agency exists")
    void isAgencyValid_exists_returnsTrue() {
        when(agencyRepository.existsById("a1")).thenReturn(true);
        assertThat(agencyService.isAgencyValid("a1")).isTrue();
    }

    @Test
    @DisplayName("isAgencyValid – returns false when agency does not exist")
    void isAgencyValid_notExists_returnsFalse() {
        when(agencyRepository.existsById("bad")).thenReturn(false);
        assertThat(agencyService.isAgencyValid("bad")).isFalse();
    }

    @Test
    @DisplayName("isVehicleValid – returns true when vehicle exists")
    void isVehicleValid_exists_returnsTrue() {
        when(vehicleRepository.existsById("v1")).thenReturn(true);
        assertThat(agencyService.isVehicleValid("v1")).isTrue();
    }

    @Test
    @DisplayName("isVehicleValid – returns false when vehicle does not exist")
    void isVehicleValid_notExists_returnsFalse() {
        when(vehicleRepository.existsById("bad")).thenReturn(false);
        assertThat(agencyService.isVehicleValid("bad")).isFalse();
    }

    // ─────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────

    private SearchRequestDto buildSearchRequest(String city) {
        return SearchRequestDto.builder()
                .sourceCity(city)
                .fromDate(FUTURE_FROM)
                .toDate(FUTURE_TO)
                .pageNumber(0)
                .pageSize(10)
                .build();
    }
}