package com.CarRentalSystem.AgencyService.Exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String s) {
        super(s);
    }
}
