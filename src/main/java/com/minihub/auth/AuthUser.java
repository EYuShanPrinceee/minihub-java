package com.minihub.auth;

import com.minihub.user.UserRole;

public class AuthUser {
    private final Long id;
    private final String nickname;
    private final String email;
    private final UserRole role;

    public AuthUser(Long id, String nickname, String email, UserRole role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}