package com.UserService.demo.Security;

public class JwtUserPrinciple{
    private String userId;
    private String username;

    public JwtUserPrinciple(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
