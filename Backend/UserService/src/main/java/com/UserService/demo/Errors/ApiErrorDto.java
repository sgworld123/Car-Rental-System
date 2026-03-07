package com.UserService.demo.Errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorDto {
    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;
}
