package com.CarRentalSystem.AuthService.Exceptions;

public class UserNameAlreadyTakenException extends RuntimeException {
    public UserNameAlreadyTakenException(String usernameAlreadyTaken) {
        super(usernameAlreadyTaken);
    }
}
