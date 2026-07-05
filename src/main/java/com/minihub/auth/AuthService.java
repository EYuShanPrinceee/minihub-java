package com.minihub.auth;

import com.minihub.exception.BusinessException;
import com.minihub.exception.ErrorCode;
import com.minihub.user.User;
import com.minihub.user.UserMapper;
import com.minihub.user.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.minihub.user.UserRole;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User existingUser = userMapper.findByEmail(request.getEmail());

        if (existingUser != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "邮箱已存在");
        }

        User user = new User();
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        userMapper.insert(user);

        User savedUser = userMapper.findById(user.getId());
        String accessToken = jwtService.generateToken(savedUser);

        return AuthResponse.of(UserResponse.from(savedUser), accessToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userMapper.findByEmail(request.getEmail());

        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "邮箱或密码错误");
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "邮箱或密码错误");
        }

        boolean passwordMatched = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatched) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "邮箱或密码错误");
        }

        String accessToken = jwtService.generateToken(user);

        return AuthResponse.of(UserResponse.from(user), accessToken);
    }

    public User profile(Long userId) {
        User user = userMapper.findById(userId);

        if (user == null) {
            throw new BusinessException(401, "用户不存在或登录已失效");
        }

        return user;
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
}