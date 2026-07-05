package com.minihub.auth;

import com.minihub.exception.BusinessException;
import com.minihub.user.User;
import com.minihub.user.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthInterceptor(JwtService jwtService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        CurrentUserContext.clear();

        String method = request.getMethod();
        String path = getPath(request);

        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        if (!needAuth(method, path)) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        String token = extractTokenFromHeader(authorization);

        Long userId = jwtService.parseUserId(token);

        User user = userMapper.findById(userId);

        if (user == null) {
            throw new BusinessException(401, "用户不存在或登录已失效");
        }

        CurrentUserContext.set(new AuthUser(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getRole()
        ));

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        CurrentUserContext.clear();
    }

    private String extractTokenFromHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new BusinessException(401, "未登录");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "Token 格式错误");
        }

        return authorization.substring(7);
    }

    private String getPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();

        if (contextPath == null || contextPath.isBlank()) {
            return requestUri;
        }

        return requestUri.substring(contextPath.length());
    }

    private boolean needAuth(String method, String path) {
        if ("GET".equalsIgnoreCase(method) && "/auth/profile".equals(path)) {
            return true;
        }

        if ("GET".equalsIgnoreCase(method) && "/users".equals(path)) {
            return true;
        }

        if (path.startsWith("/users/")
                && "DELETE".equalsIgnoreCase(method)) {
            return true;
        }

        if ("POST".equalsIgnoreCase(method) && "/articles".equals(path)) {
            return true;
        }

        if (path.startsWith("/articles/")
                && ("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))) {
            return true;
        }

        return false;
    }
}