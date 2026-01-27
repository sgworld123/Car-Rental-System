package com.CarRentalSystem.BookingService.Controller;

import com.CarRentalSystem.BookingService.Models.BookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    @PostMapping
    public BookingResponseDto createBooking()
    {
        return
    }



}
