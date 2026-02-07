package com.UserService.demo.Errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ApiErrorDto {
    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;

    public ApiErrorDto(String message, HttpStatus status) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.status = status;
    }

}
