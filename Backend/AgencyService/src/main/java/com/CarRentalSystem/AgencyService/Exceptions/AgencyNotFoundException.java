package com.CarRentalSystem.AgencyService.Exceptions;

public class AgencyNotFoundException extends RuntimeException {
    public AgencyNotFoundException(String message) {
        super(message);
    }
}
