package com.minihub.auth;

import com.minihub.exception.BusinessException;
import com.minihub.exception.ErrorCode;
import com.minihub.user.UserRole;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static void requireAdmin(AuthUser currentUser) {
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "需要管理员权限");
        }
    }
}