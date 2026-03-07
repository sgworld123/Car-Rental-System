package com.CarRentalSystem.BookingService.Exceptions;

public class VehicleUnavailableOnDatesException extends RuntimeException {
    public VehicleUnavailableOnDatesException(String message) {
        super(message);
    }
}
