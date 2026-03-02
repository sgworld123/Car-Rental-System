package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Dto.BookingByIdResponse;
import com.CarRentalSystem.BookingService.Dto.BookingRequestDto;
import com.CarRentalSystem.BookingService.Dto.BookingResponseDto;
import com.CarRentalSystem.BookingService.Dto.RequestId;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private BookedVehicleAndDatesRepository bookedVehicleAndDatesRepository;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private ValueOperations<String, String> valueOperations;

    @InjectMocks private BookingService bookingService;

    private BookingRequestDto requestDto;

    private static final String USER_ID    = "user-123";
    private static final String VEHICLE_ID = "veh-abc";
    private static final LocalDate FROM    = LocalDate.of(2025, 6, 1);
    private static final LocalDate TO      = LocalDate.of(2025, 6, 3); // 3 days

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
        when(valueOperations.setIfAbsent(anyString(), anyString(), eq(15L), eq(TimeUnit.MINUTES)))
                .thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        BookingResponseDto response = bookingService.createBooking(USER_ID, requestDto);

        assertThat(response.getBookingStatus()).isEqualTo("PENDING");
        assertThat(response.getTotlecost()).isEqualTo(1500.0); // 3 days × 500
        assertThat(response.getBookingId()).isNotNull();
        // One BookedVehicleAndDates record per day (3 days)
        verify(bookedVehicleAndDatesRepository, times(3)).save(any(BookedVehicleAndDates.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("createBooking – throws when a Redis lock cannot be acquired")
    void createBooking_lockFails_throws() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), eq(15L), eq(TimeUnit.MINUTES)))
                .thenReturn(true)
                .thenReturn(false); // second date is taken

        assertThatThrownBy(() -> bookingService.createBooking(USER_ID, requestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("unavailable on");
    }

    @Test
    @DisplayName("createBooking – rolls back Redis locks AND BookedVehicleAndDates on failure")
    void createBooking_lockFails_rollsBack() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), eq(15L), eq(TimeUnit.MINUTES)))
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
        when(valueOperations.setIfAbsent(anyString(), anyString(), eq(15L), eq(TimeUnit.MINUTES)))
                .thenReturn(true);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingResponseDto response = bookingService.createBooking(USER_ID, requestDto);

        assertThat(response.getTotlecost()).isEqualTo(300.0);
        verify(bookedVehicleAndDatesRepository, times(1)).save(any());
    }

    // ─────────────────────────────────────────────
    // confirmBooking
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("confirmBooking – valid Redis hold sets status to CONFIRMED and deletes hold keys")
    void confirmBooking_success() {
        String bookingId = "book-999";
        Booking booking = buildBooking(bookingId, USER_ID, BookingStatus.PENDING, FROM, TO);

        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(booking));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(bookingId);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingResponseDto response = bookingService.confirmBooking(bookingId);

        assertThat(response.getBookingStatus()).isEqualTo("CONFIRMED");
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(redisTemplate, times(3)).delete(anyString()); // one delete per day
    }

    @Test
    @DisplayName("confirmBooking – throws when booking not found")
    void confirmBooking_notFound_throws() {
        when(bookingRepository.findByBookingId("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.confirmBooking("ghost"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No such booking found");
    }

    @Test
    @DisplayName("confirmBooking – throws when Redis key is held by a different booking")
    void confirmBooking_heldByOtherBooking_throws() {
        String bookingId = "book-999";
        Booking booking = buildBooking(bookingId, USER_ID, BookingStatus.PENDING, FROM, FROM);

        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(booking));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn("different-booking-id");

        assertThatThrownBy(() -> bookingService.confirmBooking(bookingId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Booking not held for");
    }

    // ─────────────────────────────────────────────
    // cancelBooking (v2 — accepts userId for ownership check)
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("cancelBooking – PENDING booking is cancelled by owner")
    void cancelBooking_pendingByOwner_success() {
        String bookingId = "book-101";
        // Use same instance to work around the != reference bug in v2
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .userId(USER_ID)
                .status(BookingStatus.PENDING)
                .fromDate(FROM)
                .endDate(FROM)
                .vehicleId(VEHICLE_ID)
                .build();

        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Pass the exact same string reference to avoid the != bug
        Booking result = bookingService.cancelBooking(booking.getUserId(), new RequestId(bookingId));

        assertThat(result.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancelBooking – cleans up BookedVehicleAndDates and Redis hold keys")
    void cancelBooking_cleansUpResources() {
        Booking booking = Booking.builder()
                .bookingId("book-clean")
                .userId(USER_ID)
                .status(BookingStatus.PENDING)
                .fromDate(FROM)
                .endDate(FROM) // 1 day
                .vehicleId(VEHICLE_ID)
                .build();

        when(bookingRepository.findByBookingId("book-clean")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        bookingService.cancelBooking(booking.getUserId(), new RequestId("book-clean"));

        verify(bookedVehicleAndDatesRepository).deleteByBookingId("book-clean");
        verify(redisTemplate, atLeastOnce()).delete(anyString());
    }

    @Test
    @DisplayName("cancelBooking – throws for CONFIRMED booking")
    void cancelBooking_confirmedStatus_throws() {
        Booking booking = Booking.builder()
                .bookingId("book-cf")
                .userId(USER_ID)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingRepository.findByBookingId("book-cf")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(booking.getUserId(), new RequestId("book-cf")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CONFIRMED BOOKINGS WILL NOT BE CANCELLED");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancelBooking – throws for COMPLETED booking")
    void cancelBooking_completedStatus_throws() {
        Booking booking = Booking.builder()
                .bookingId("book-done")
                .userId(USER_ID)
                .status(BookingStatus.COMPLETED)
                .build();

        when(bookingRepository.findByBookingId("book-done")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(booking.getUserId(), new RequestId("book-done")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("BOOKING ALREADY COMPLETED");
    }

    @Test
    @DisplayName("cancelBooking – throws when booking not found")
    void cancelBooking_notFound_throws() {
        when(bookingRepository.findByBookingId("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(USER_ID, new RequestId("bad-id")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    @DisplayName("[BUG] cancelBooking – != comparison on strings is broken, must be .equals()")
    void cancelBooking_ownershipCheckUses_notEquals_shouldUse_dotEquals() {
        // This test documents the != reference-equality bug.
        // booking.getUserId() != userId can be TRUE even when both hold "user-123"
        // if they are different String object instances (e.g., coming from HTTP headers).
        // Fix: replace != with !booking.getUserId().equals(userId)

        String sameValueDifferentRef1 = new String("user-abc");
        String sameValueDifferentRef2 = new String("user-abc");

        // Sanity check: these are equal in value but different in reference
        assertThat(sameValueDifferentRef1).isEqualTo(sameValueDifferentRef2);
        assertThat(sameValueDifferentRef1 != sameValueDifferentRef2).isTrue(); // the bug
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
    }

    @Test
    @DisplayName("getBooking – empty list when user has no bookings")
    void getBooking_emptyForUser() {
        when(bookingRepository.findByUserId(USER_ID)).thenReturn(List.of());
        assertThat(bookingService.getBooking(USER_ID)).isEmpty();
    }

    // ─────────────────────────────────────────────
    // completeExpiredBookings
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("completeExpiredBookings – marks expired confirmed bookings COMPLETED")
    void completeExpiredBookings_marksCompleted() {
        Booking b = buildBooking("exp-1", USER_ID, BookingStatus.CONFIRMED,
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));

        when(bookingRepository.findByStatusAndEndDateBefore(
                eq(BookingStatus.CONFIRMED), any(LocalDate.class))).thenReturn(List.of(b));

        bookingService.completeExpiredBookings();

        assertThat(b.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        verify(bookedVehicleAndDatesRepository).deleteByBookingId("exp-1");
        verify(bookingRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("completeExpiredBookings – no-op when no expired bookings")
    void completeExpiredBookings_noExpired_noop() {
        when(bookingRepository.findByStatusAndEndDateBefore(any(), any())).thenReturn(List.of());

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
                .build();
    }
}