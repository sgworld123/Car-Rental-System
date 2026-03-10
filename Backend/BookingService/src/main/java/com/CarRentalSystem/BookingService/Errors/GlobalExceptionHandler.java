package com.CarRentalSystem.BookingService.Errors;

import com.CarRentalSystem.BookingService.Exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {

        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(ApiError.builder()
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    // Catch everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobal(Exception ex) {
        ApiError error = ApiError.builder()
                .message("Internal server error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.status(500).body(error);
    }
    @ExceptionHandler(VehicleUnavailableOnDatesException.class)
    public ResponseEntity<ApiError> handleVehicleUnavailableOnDateException(VehicleUnavailableOnDatesException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(UserIsUnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorization(UserIsUnauthorizedException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .build());
    }
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiError> handleBookingNotFound(BookingNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .build());
    }
    @ExceptionHandler(BookingAlreadyCompletedException.class)
    public ResponseEntity<ApiError> handleBookingAlreadyCompleted(BookingAlreadyCompletedException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .build());
    }
    @ExceptionHandler(BookingIsAlreadyCancelledException.class)
    public ResponseEntity<ApiError> handleBookingAlreadyCancelled(BookingIsAlreadyCancelledException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .build());
    }


}
