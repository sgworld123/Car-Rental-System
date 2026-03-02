package com.CarRentalSystem.BookingService.Controller;

import com.CarRentalSystem.BookingService.Dto.BookingByIdResponse;
import com.CarRentalSystem.BookingService.Dto.BookingRequestDto;
import com.CarRentalSystem.BookingService.Dto.BookingResponseDto;
import com.CarRentalSystem.BookingService.Dto.RequestId;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Service.BookingService;
import com.CarRentalSystem.BookingService.Service.RedisTestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock private BookingService bookingService;
    @Mock private RedisTestService redisTestService;
    @InjectMocks private BookingController bookingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("POST /api/booking – creates booking, returns 200 with PENDING status")
    void createBooking_returns200() throws Exception {
        BookingRequestDto req = BookingRequestDto.builder()
                .vehicleId("veh-1")
                .fromDate(LocalDate.of(2025, 8, 1))
                .toDate(LocalDate.of(2025, 8, 3))
                .cost(400)
                .build();

        BookingResponseDto resp = BookingResponseDto.builder()
                .BookingId("book-xyz")
                .bookingStatus("PENDING")
                .totlecost(1200.0)
                .build();

        when(bookingService.createBooking(eq("user-1"), any())).thenReturn(resp);

        mockMvc.perform(post("/api/booking")
                        .header("X-User-Id", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingStatus").value("PENDING"))
                .andExpect(jsonPath("$.totlecost").value(1200.0));
    }

    @Test
    @DisplayName("PUT /api/booking/confirm/{id} – returns CONFIRMED status")
    void confirmBooking_returns200() throws Exception {
        BookingResponseDto resp = BookingResponseDto.builder()
                .BookingId("book-xyz")
                .bookingStatus("CONFIRMED")
                .totlecost(1200.0)
                .build();

        when(bookingService.confirmBooking("book-xyz")).thenReturn(resp);

        mockMvc.perform(put("/api/booking/confirm/book-xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingStatus").value("CONFIRMED"));
    }

    @Test
    @DisplayName("PUT /api/booking/cancel – now requires X-User-Id header for ownership check")
    void cancelBooking_withUserId_returns200() throws Exception {
        Booking cancelled = Booking.builder()
                .bookingId("book-xyz")
                .status(BookingStatus.CANCELLED)
                .build();

        // v2: cancelBooking now takes userId as first arg
        when(bookingService.cancelBooking(eq("user-1"), any(RequestId.class))).thenReturn(cancelled);

        mockMvc.perform(put("/api/booking/cancel")
                        .header("X-User-Id", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RequestId("book-xyz"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("GET /api/booking/my – returns user's booking list")
    void getBooking_returnsUserBookings() throws Exception {
        BookingByIdResponse b = BookingByIdResponse.builder()
                .bookingId("b1")
                .vehicleId("veh-1")
                .cost(900.0)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingService.getBooking("user-1")).thenReturn(List.of(b));

        mockMvc.perform(get("/api/booking/my")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value("b1"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }
}