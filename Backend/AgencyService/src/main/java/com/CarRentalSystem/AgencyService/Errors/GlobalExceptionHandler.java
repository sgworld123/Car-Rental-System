package com.CarRentalSystem.AgencyService.Errors;

import com.CarRentalSystem.AgencyService.Exceptions.AgencyNotFoundException;
import com.CarRentalSystem.AgencyService.Exceptions.InvalidDatesException;
import com.CarRentalSystem.AgencyService.Exceptions.VehicleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AgencyNotFoundException.class)
    public ResponseEntity<ErrorDto> handleAgencyNotFoundException(AgencyNotFoundException ex) {
        return ResponseEntity.status(404).body(ErrorDto.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorDto> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        return ResponseEntity.status(404).body(ErrorDto.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<ErrorDto> invalidDatesException(InvalidDatesException ex) {
        return ResponseEntity.status(400).body(ErrorDto.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

}
