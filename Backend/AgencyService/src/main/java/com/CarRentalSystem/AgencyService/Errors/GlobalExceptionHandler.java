package com.CarRentalSystem.AgencyService.Errors;

import com.CarRentalSystem.AgencyService.Exceptions.AgencyNotFoundException;
import com.CarRentalSystem.AgencyService.Exceptions.VehicleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
@ControllerAdvice
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
}
