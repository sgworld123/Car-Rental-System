package com.CarRentalSystem.BookingService.Errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private String message;
    private int status;
    private LocalDateTime timestamp;;

    public ApiError(String message, int statusCode) {
        this.message = message;
        this.status = statusCode;
        this.timestamp = LocalDateTime.now();
    }
}
