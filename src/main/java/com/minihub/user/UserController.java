package com.minihub.user;

import com.minihub.common.ApiResponse;
import com.minihub.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.minihub.auth.AuthUser;
import com.minihub.auth.CurrentUser;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.create(request);
        return ApiResponse.success(UserResponse.from(user), "创建用户成功");
    }

    @Operation(summary = "通过邮箱查询用户")
    @GetMapping("/by-email")
    public ApiResponse<UserResponse> findByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return ApiResponse.success(UserResponse.from(user), "查询用户成功");
    }

    @Operation(summary = "通过id查询用户")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponse.success(UserResponse.from(user), "查询用户成功");
    }

    @Operation(summary = "用户分页查询")
    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> findPage(
            @CurrentUser AuthUser currentUser,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        PageResponse<User> pageData = userService.findPage(page, pageSize, currentUser);

        List<UserResponse> records = pageData.getRecords()
                .stream()
                .map(UserResponse::from)
                .toList();

        PageResponse<UserResponse> result = PageResponse.of(
                records,
                pageData.getTotal(),
                pageData.getPage(),
                pageData.getPageSize()
        );

        return ApiResponse.success(result, "查询用户列表成功");
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        User user = userService.update(id, request);
        return ApiResponse.success(UserResponse.from(user), "更新用户成功");
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(
            @CurrentUser AuthUser currentUser,
            @PathVariable Long id
    ) {
        userService.delete(id, currentUser);
        return ApiResponse.success(null, "删除用户成功");
    }
}