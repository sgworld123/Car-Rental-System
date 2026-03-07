package com.UserService.demo.Errors;

import com.UserService.demo.Dto.SignupResponseDto;
import com.UserService.demo.Exceptions.UserNameAlreadyTakenException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

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
}
