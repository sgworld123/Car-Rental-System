package com.CarRentalSystem.BookingService.Controller;

import com.CarRentalSystem.BookingService.Dto.BookingByIdResponse;
import com.CarRentalSystem.BookingService.Dto.BookingRequestDto;
import com.CarRentalSystem.BookingService.Dto.BookingResponseDto;
import com.CarRentalSystem.BookingService.Dto.RequestId;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestHeader("X-User-Id") String userId
            ,@RequestBody BookingRequestDto bookingRequestDto) {
        return ResponseEntity.ok(bookingService.createBooking(userId,bookingRequestDto));
    }
    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<BookingResponseDto> confirmBooking(@RequestHeader("X-User-Id") String userId, @PathVariable String bookingId)
    {
        return ResponseEntity.ok(bookingService.confirmBooking(userId,bookingId));
    }
    @PutMapping("/cancel")
    public BookingResponseDto cancelBooking(@RequestHeader("X-User-Id") String userId,@RequestBody RequestId bookingId)
    {
        return bookingService.cancelBooking(userId,bookingId);
    }
    @GetMapping("/my")
    public List<BookingByIdResponse> getBooking(@RequestHeader("X-User-Id") String userId)
    {
        return bookingService.getBooking(userId);
    }
}
