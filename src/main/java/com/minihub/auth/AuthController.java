package com.minihub.auth;

import com.minihub.common.ApiResponse;
import com.minihub.user.User;
import com.minihub.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "认证模块")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ApiResponse.success(response, "注册成功");
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ApiResponse.success(response, "登录成功");
    }

    @Operation(summary = "获取当前用户")
    @GetMapping("/profile")
    public ApiResponse<UserResponse> profile(@CurrentUser AuthUser currentUser) {
        User user = authService.profile(currentUser.getId());
        return ApiResponse.success(UserResponse.from(user), "获取当前用户成功");
    }
}