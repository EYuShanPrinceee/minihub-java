package com.minihub.user;

public class UserResponse {
    private Long id;
    private String nickname;
    private String email;
    private UserRole role;
    private Long articleCount;

    public UserResponse(
            Long id,
            String nickname,
            String email,
            UserRole role,
            Long articleCount
    ) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.articleCount = articleCount;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getRole(),
                user.getArticleCount()
        );
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

    public Long getArticleCount() {
        return articleCount;
    }
}