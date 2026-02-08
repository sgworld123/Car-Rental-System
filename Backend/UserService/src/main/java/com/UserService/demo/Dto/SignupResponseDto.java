package com.UserService.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {
    private String id;
    private String username;

    @Data
    public static class ApiErrorDto {
        private LocalDateTime timestamp;
        private String message;
        private HttpStatus status;

        public ApiErrorDto(String message, HttpStatus status) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.status = status;
        }

    }
}
