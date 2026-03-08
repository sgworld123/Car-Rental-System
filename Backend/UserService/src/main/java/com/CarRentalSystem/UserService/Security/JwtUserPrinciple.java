package com.CarRentalSystem.UserService.Security;

import lombok.Data;
import lombok.Getter;

@Getter
public class JwtUserPrinciple{
    private final String userId;
    private final String username;

    public JwtUserPrinciple(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

}
