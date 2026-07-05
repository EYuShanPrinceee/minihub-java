package com.minihub.auth;

public final class CurrentUserContext {
    private static final ThreadLocal<AuthUser> HOLDER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(AuthUser user) {
        HOLDER.set(user);
    }

    public static AuthUser get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}