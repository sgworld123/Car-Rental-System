package com.UserService.demo.Errors;

import com.UserService.demo.Dto.SignupResponseDto;
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
    public ResponseEntity<SignupResponseDto.ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        SignupResponseDto.ApiErrorDto errorDto = new SignupResponseDto.ApiErrorDto("User not found" + ex.getMessage() , HttpStatus.NOT_FOUND);
        return ResponseEntity.status(404).body(errorDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<SignupResponseDto.ApiErrorDto> handleAuthenticationException(AuthenticationException ex) {
        SignupResponseDto.ApiErrorDto apiError = new SignupResponseDto.ApiErrorDto("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<SignupResponseDto.ApiErrorDto> handleJwtException(JwtException ex) {
        SignupResponseDto.ApiErrorDto apiError = new SignupResponseDto.ApiErrorDto("Invalid JWT token: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<SignupResponseDto.ApiErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        SignupResponseDto.ApiErrorDto apiError = new SignupResponseDto.ApiErrorDto("Access denied: Insufficient permissions", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SignupResponseDto.ApiErrorDto> handleGenericException(Exception ex) {
        SignupResponseDto.ApiErrorDto errorDto = new SignupResponseDto.ApiErrorDto("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(500).body(errorDto);
    }
}
