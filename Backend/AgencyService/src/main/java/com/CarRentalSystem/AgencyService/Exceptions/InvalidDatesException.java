package com.CarRentalSystem.AgencyService.Exceptions;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
