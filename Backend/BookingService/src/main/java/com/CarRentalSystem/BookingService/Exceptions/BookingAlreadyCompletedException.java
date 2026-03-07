package com.CarRentalSystem.BookingService.Exceptions;

public class BookingAlreadyCompletedException extends RuntimeException {
    public BookingAlreadyCompletedException(String bookingAlreadyCompleted) {
        super(bookingAlreadyCompleted);
    }
}
