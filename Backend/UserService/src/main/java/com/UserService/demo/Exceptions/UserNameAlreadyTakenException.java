package com.UserService.demo.Exceptions;

public class UserNameAlreadyTakenException extends RuntimeException {
    public UserNameAlreadyTakenException(String usernameAlreadyTaken) {
        super(usernameAlreadyTaken);
    }
}
