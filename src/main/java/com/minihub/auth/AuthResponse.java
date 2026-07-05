package com.minihub.auth;

import com.minihub.user.UserResponse;

public class AuthResponse {
    private UserResponse user;
    private String accessToken;
    private String tokenType;

    public AuthResponse(UserResponse user, String accessToken, String tokenType) {
        this.user = user;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public static AuthResponse of(UserResponse user, String accessToken) {
        return new AuthResponse(user, accessToken, "Bearer");
    }

    public UserResponse getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}