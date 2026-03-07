package com.CarRentalSystem.BookingService.Exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String s) {
        super(s);
    }
}
