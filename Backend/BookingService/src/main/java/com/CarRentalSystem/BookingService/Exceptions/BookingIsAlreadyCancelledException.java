package com.CarRentalSystem.BookingService.Exceptions;

public class BookingIsAlreadyCancelledException extends RuntimeException {
    public BookingIsAlreadyCancelledException(String bookingAlreadyCancelled) {
        super(bookingAlreadyCancelled);
    }
}
