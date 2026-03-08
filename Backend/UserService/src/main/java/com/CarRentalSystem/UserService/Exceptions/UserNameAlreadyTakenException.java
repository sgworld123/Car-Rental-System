package com.CarRentalSystem.UserService.Exceptions;

public class UserNameAlreadyTakenException extends RuntimeException {
    public UserNameAlreadyTakenException(String usernameAlreadyTaken) {
        super(usernameAlreadyTaken);
    }
}
