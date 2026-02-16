package com.CarRentalSystem.BookingService.Controller;

import com.CarRentalSystem.BookingService.Dto.BookingByIdResponse;
import com.CarRentalSystem.BookingService.Dto.BookingRequestDto;
import com.CarRentalSystem.BookingService.Dto.BookingResponseDto;
import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;
    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader("X-User-Id") String userId
            ,@RequestBody BookingRequestDto bookingRequestDto) {
        System.out.println("USERID = " + userId);
        return bookingService.createBooking(userId,bookingRequestDto);
    }
    @PutMapping("/confirm/{bookingId}")
    public BookingResponseDto confirmBooking(@PathVariable String bookingId)
    {
        return bookingService.confirmBooking(bookingId);
    }
    @PutMapping("/cancel")
    public Booking cancelBooking(@RequestBody String bookingId)
    {
        return bookingService.cancelBooking(bookingId);
    }
    @GetMapping("/my")
    public BookingByIdResponse getBooking(@RequestHeader("X-User-Id") String userId)
    {
        return bookingService.getBooking(userId);
    }
}
