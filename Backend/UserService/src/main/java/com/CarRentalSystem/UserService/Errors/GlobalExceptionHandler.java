package com.CarRentalSystem.UserService.Errors;

import com.CarRentalSystem.UserService.Exceptions.ExpiredJwtException;
import com.CarRentalSystem.UserService.Exceptions.UserNameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(UserNameAlreadyTakenException.class)
    public ResponseEntity<ApiErrorDto> handleUserNameAlreadyTakenException(UserNameAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorDto> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }
}
