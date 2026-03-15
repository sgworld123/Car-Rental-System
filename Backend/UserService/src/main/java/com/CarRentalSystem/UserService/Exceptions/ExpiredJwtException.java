package com.CarRentalSystem.UserService.Exceptions;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException(String message) {
        super(message);
    }
}
