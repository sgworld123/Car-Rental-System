package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Dto.*;
import com.CarRentalSystem.BookingService.Exceptions.*;
import com.CarRentalSystem.BookingService.Models.BookedVehicleAndDates;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import com.CarRentalSystem.BookingService.Repository.BookedVehicleAndDatesRepository;
import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private BookedVehicleAndDatesRepository bookedVehicleAndDatesRepository;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private ValueOperations<String, String> valueOperations;
    @Mock private BookingEventPublisher bookingEventPublisher;

    @InjectMocks private BookingService bookingService;

    private static final String USER_ID    = "user-123";
    private static final String OTHER_USER = "user-999";
    private static final String VEHICLE_ID = "veh-abc";
    private static final LocalDate FROM    = LocalDate.of(2025, 6, 1);
    private static final LocalDate TO      = LocalDate.of(2025, 6, 3); // 3 days

    private BookingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = BookingRequestDto.builder()
                .vehicleId(VEHICLE_ID)
                .fromDate(FROM)
                .toDate(TO)
                .cost(500)
                .build();
    }

    // ─────────────────────────────────────────────
    // createBooking
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("createBooking – acquires Redis locks, persists booking and BookedVehicleAndDates")
    void createBooking_success_savesAllData() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
                .thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        BookingResponseDto response = bookingService.createBooking(USER_ID, requestDto);

        assertThat(response.getBookingStatus()).isEqualTo("PENDING");
        assertThat(response.getTotalCost()).isEqualTo(1500.0); // 3 days × 500
        assertThat(response.getBookingId()).isNotNull();
        // One BookedVehicleAndDates record per day (3 days)
        verify(bookedVehicleAndDatesRepository, times(3)).save(any(BookedVehicleAndDates.class));
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("createBooking – throws VehicleUnavailableOnDatesException when Redis lock fails")
    void createBooking_lockFails_throws() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
                .thenReturn(true)
                .thenReturn(false); // second date is taken

        assertThatThrownBy(() -> bookingService.createBooking(USER_ID, requestDto))
                .isInstanceOf(VehicleUnavailableOnDatesException.class)
                .hasMessageContaining("unavailable on");
    }

    @Test
    @DisplayName("createBooking – rolls back Redis locks and BookedVehicleAndDates on lock failure")
    void createBooking_lockFails_rollsBack() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
                .thenReturn(true)
                .thenReturn(false);

        assertThatThrownBy(() -> bookingService.createBooking(USER_ID, requestDto));

        verify(redisTemplate).delete(anyList());
        verify(bookedVehicleAndDatesRepository).deleteByBookingId(anyString());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBooking – single-day booking saves exactly one BookedVehicleAndDates record")
    void createBooking_singleDay_savesOneRecord() {
        requestDto.setFromDate(FROM);
        requestDto.setToDate(FROM);
        requestDto.setCost(300);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
                .thenReturn(true);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingResponseDto response = bookingService.createBooking(USER_ID, requestDto);

        assertThat(response.getTotalCost()).isEqualTo(300.0);
        verify(bookedVehicleAndDatesRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("createBooking – booking is saved with PENDING status and correct userId")
    void createBooking_bookingSavedWithCorrectFields() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any())).thenReturn(true);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        bookingService.createBooking(USER_ID, requestDto);

        verify(bookingRepository).save(argThat(b ->
                b.getUserId().equals(USER_ID) &&
                        b.getStatus() == BookingStatus.PENDING &&
                        b.getVehicleId().equals(VEHICLE_ID)
        ));
    }

    // ─────────────────────────────────────────────
    // confirmBooking
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("confirmBooking – PENDING booking publishes BookingCreatedEvent")
    void confirmBooking_pending_publishesEvent() {
        Booking booking = buildBooking("book-1", USER_ID, BookingStatus.PENDING, FROM, TO);
        when(bookingRepository.findByBookingId("book-1")).thenReturn(Optional.of(booking));

        BookingResponseDto response = bookingService.confirmBooking(USER_ID, "book-1");

        verify(bookingEventPublisher).handleBookingCreated(argThat(e ->
                e.getBookingId().equals("book-1") &&
                        e.getUserId().equals(USER_ID)
        ));
        assertThat(response.getBookingId()).isEqualTo("book-1");
    }

    @Test
    @DisplayName("confirmBooking – throws BookingNotFoundException for unknown bookingId")
    void confirmBooking_notFound_throws() {
        when(bookingRepository.findByBookingId("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.confirmBooking(USER_ID, "bad-id"))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("bad-id");
    }

    @Test
    @DisplayName("confirmBooking – throws UserIsUnauthorizedException when userId does not match")
    void confirmBooking_wrongUser_throws() {
        Booking booking = buildBooking("book-1", USER_ID, BookingStatus.PENDING, FROM, TO);
        when(bookingRepository.findByBookingId("book-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.confirmBooking(OTHER_USER, "book-1"))
                .isInstanceOf(UserIsUnauthorizedException.class);
    }

    @Test
    @DisplayName("confirmBooking – throws RuntimeException when booking is already CONFIRMED")
    void confirmBooking_alreadyConfirmed_throws() {
        Booking booking = buildBooking("book-1", USER_ID, BookingStatus.CONFIRMED, FROM, TO);
        when(bookingRepository.findByBookingId("book-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.confirmBooking(USER_ID, "book-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("BOOKING ALREADY CONFIRMED");
    }

    @Test
    @DisplayName("confirmBooking – throws RuntimeException when booking is CANCELLED")
    void confirmBooking_cancelled_throws() {
        Booking booking = buildBooking("book-1", USER_ID, BookingStatus.CANCELLED, FROM, TO);
        when(bookingRepository.findByBookingId("book-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.confirmBooking(USER_ID, "book-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CANCELLED BOOKING CANNOT BE CONFIRMED");
    }

    @Test
    @DisplayName("confirmBooking – throws BookingAlreadyCompletedException when booking is COMPLETED")
    void confirmBooking_completed_throws() {
        Booking booking = buildBooking("book-1", USER_ID, BookingStatus.COMPLETED, FROM, TO);
        when(bookingRepository.findByBookingId("book-1")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.confirmBooking(USER_ID, "book-1"))
                .isInstanceOf(BookingAlreadyCompletedException.class);
    }

    // ─────────────────────────────────────────────
    // cancelBooking
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("cancelBooking – CONFIRMED booking publishes BookingCancelledEvent and cleans up")
    void cancelBooking_confirmed_publishesEventAndCleansUp() {
        Booking booking = buildBooking("book-2", USER_ID, BookingStatus.CONFIRMED, FROM, TO);
        when(bookingRepository.findByBookingId("book-2")).thenReturn(Optional.of(booking));

        BookingResponseDto response = bookingService.cancelBooking(USER_ID, new RequestId("book-2"));

        verify(bookedVehicleAndDatesRepository).deleteByBookingId("book-2");
        verify(bookingEventPublisher).handleBookingCancelled(argThat(e ->
                e.getBookingId().equals("book-2") &&
                        e.getUserId().equals(USER_ID)
        ));
        assertThat(response.getBookingStatus()).isEqualTo("CANCELLED");
    }

    @Test
    @DisplayName("cancelBooking – throws BookingNotFoundException for unknown bookingId")
    void cancelBooking_notFound_throws() {
        when(bookingRepository.findByBookingId("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(USER_ID, new RequestId("bad-id")))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    @DisplayName("cancelBooking – throws UserIsUnauthorizedException when userId does not match")
    void cancelBooking_wrongUser_throws() {
        Booking booking = buildBooking("book-2", USER_ID, BookingStatus.CONFIRMED, FROM, TO);
        when(bookingRepository.findByBookingId("book-2")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(OTHER_USER, new RequestId("book-2")))
                .isInstanceOf(UserIsUnauthorizedException.class);

        verify(bookingEventPublisher, never()).handleBookingCancelled(any());
    }

    @Test
    @DisplayName("cancelBooking – throws BookingIsAlreadyCancelledException when already CANCELLED")
    void cancelBooking_alreadyCancelled_throws() {
        Booking booking = buildBooking("book-2", USER_ID, BookingStatus.CANCELLED, FROM, TO);
        when(bookingRepository.findByBookingId("book-2")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(USER_ID, new RequestId("book-2")))
                .isInstanceOf(BookingIsAlreadyCancelledException.class)
                .hasMessageContaining("BOOKING ALREADY CANCELLED");
    }

    @Test
    @DisplayName("cancelBooking – throws RuntimeException when booking is PENDING")
    void cancelBooking_pending_throws() {
        Booking booking = buildBooking("book-2", USER_ID, BookingStatus.PENDING, FROM, TO);
        when(bookingRepository.findByBookingId("book-2")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(USER_ID, new RequestId("book-2")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PENDING BOOKING CANNOT BE CANCELLED");
    }

    @Test
    @DisplayName("cancelBooking – throws BookingAlreadyCompletedException when booking is COMPLETED")
    void cancelBooking_completed_throws() {
        Booking booking = buildBooking("book-2", USER_ID, BookingStatus.COMPLETED, FROM, TO);
        when(bookingRepository.findByBookingId("book-2")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(USER_ID, new RequestId("book-2")))
                .isInstanceOf(BookingAlreadyCompletedException.class)
                .hasMessageContaining("BOOKING ALREADY COMPLETED");
    }

    // ─────────────────────────────────────────────
    // getCost
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getCost – 3 days × 500 = 1500")
    void getCost_multiDay() {
        assertThat(bookingService.getCost(requestDto)).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("getCost – same start and end date = 1 day")
    void getCost_singleDay() {
        requestDto.setFromDate(FROM);
        requestDto.setToDate(FROM);
        requestDto.setCost(250);
        assertThat(bookingService.getCost(requestDto)).isEqualTo(250.0);
    }

    @Test
    @DisplayName("getCost – 7 days calculates correctly")
    void getCost_sevenDays() {
        requestDto.setFromDate(FROM);
        requestDto.setToDate(FROM.plusDays(6));
        requestDto.setCost(100);
        assertThat(bookingService.getCost(requestDto)).isEqualTo(700.0);
    }

    // ─────────────────────────────────────────────
    // getBooking
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getBooking – returns all fields correctly mapped")
    void getBooking_returnsMappedList() {
        Booking b = buildBooking("b1", USER_ID, BookingStatus.CONFIRMED, FROM, TO);
        b.setCost(1500);
        when(bookingRepository.findByUserId(USER_ID)).thenReturn(List.of(b));

        List<BookingByIdResponse> result = bookingService.getBooking(USER_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookingId()).isEqualTo("b1");
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(result.get(0).getCost()).isEqualTo(1500);
        assertThat(result.get(0).getFromDate()).isEqualTo(FROM);
        assertThat(result.get(0).getEndDate()).isEqualTo(TO);
        assertThat(result.get(0).getVehicleId()).isEqualTo(VEHICLE_ID);
    }

    @Test
    @DisplayName("getBooking – returns empty list when user has no bookings")
    void getBooking_emptyForUser() {
        when(bookingRepository.findByUserId(USER_ID)).thenReturn(List.of());
        assertThat(bookingService.getBooking(USER_ID)).isEmpty();
    }

    @Test
    @DisplayName("getBooking – returns multiple bookings for same user")
    void getBooking_multipleBookings() {
        Booking b1 = buildBooking("b1", USER_ID, BookingStatus.CONFIRMED, FROM, TO);
        Booking b2 = buildBooking("b2", USER_ID, BookingStatus.PENDING, FROM.plusDays(5), TO.plusDays(5));
        when(bookingRepository.findByUserId(USER_ID)).thenReturn(List.of(b1, b2));

        List<BookingByIdResponse> result = bookingService.getBooking(USER_ID);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(BookingByIdResponse::getBookingId)
                .containsExactly("b1", "b2");
    }

    // ─────────────────────────────────────────────
    // completeExpiredBookings
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("completeExpiredBookings – marks expired CONFIRMED bookings as COMPLETED")
    void completeExpiredBookings_marksCompleted() {
        Booking b = buildBooking("exp-1", USER_ID, BookingStatus.CONFIRMED,
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));

        when(bookingRepository.findByStatusAndEndDateBefore(
                eq(BookingStatus.CONFIRMED), any(LocalDate.class)))
                .thenReturn(List.of(b));

        bookingService.completeExpiredBookings();

        assertThat(b.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        assertThat(b.getUpdatedAt()).isNotNull();
        verify(bookedVehicleAndDatesRepository).deleteByBookingId("exp-1");
        verify(bookingRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("completeExpiredBookings – marks multiple expired bookings")
    void completeExpiredBookings_multipleExpired() {
        Booking b1 = buildBooking("exp-1", USER_ID, BookingStatus.CONFIRMED,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(3));
        Booking b2 = buildBooking("exp-2", USER_ID, BookingStatus.CONFIRMED,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));

        when(bookingRepository.findByStatusAndEndDateBefore(any(), any()))
                .thenReturn(List.of(b1, b2));

        bookingService.completeExpiredBookings();

        assertThat(b1.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        assertThat(b2.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        verify(bookedVehicleAndDatesRepository).deleteByBookingId("exp-1");
        verify(bookedVehicleAndDatesRepository).deleteByBookingId("exp-2");
        verify(bookingRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("completeExpiredBookings – no-op when no expired bookings")
    void completeExpiredBookings_noExpired_noop() {
        when(bookingRepository.findByStatusAndEndDateBefore(any(), any()))
                .thenReturn(List.of());

        bookingService.completeExpiredBookings();

        verify(bookingRepository).saveAll(List.of());
        verify(bookedVehicleAndDatesRepository, never()).deleteByBookingId(any());
    }

    // ─────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────

    private Booking buildBooking(String bookingId, String userId, BookingStatus status,
                                 LocalDate from, LocalDate to) {
        return Booking.builder()
                .bookingId(bookingId)
                .userId(userId)
                .vehicleId(VEHICLE_ID)
                .fromDate(from)
                .endDate(to)
                .status(status)
                .cost(500)
                .build();
    }
}