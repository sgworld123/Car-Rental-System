package com.CarRentalSystem.BookingService.Exceptions;

public class UserIsUnauthorizedException extends RuntimeException {
    public UserIsUnauthorizedException(String unauthorizedConfirmationAttempt) {
        super(unauthorizedConfirmationAttempt);
    }
}
