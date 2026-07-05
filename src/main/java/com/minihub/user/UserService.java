package com.minihub.user;

import com.minihub.article.ArticleMapper;
import com.minihub.common.PageResponse;
import com.minihub.common.PageUtils;
import com.minihub.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.minihub.auth.AuthUser;
import com.minihub.auth.PermissionUtils;
import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;

    public UserService(UserMapper userMapper, ArticleMapper articleMapper) {
        this.userMapper = userMapper;
        this.articleMapper = articleMapper;
    }

    public User create(CreateUserRequest request) {
        User existingUser = userMapper.findByEmail(request.getEmail());

        if (existingUser != null) {
            throw new BusinessException(409, "邮箱已存在");
        }

        User user = new User();
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPasswordHash("");
        user.setRole(UserRole.USER);

        userMapper.insert(user);

        return findById(user.getId());
    }

    public User findById(Long id) {
        User user = userMapper.findById(id);

        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        return user;
    }

    public User findByEmail(String email) {
        User user = userMapper.findByEmail(email);

        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        return user;
    }

    public PageResponse<User> findPage(Integer page, Integer pageSize, AuthUser currentUser) {
        PermissionUtils.requireAdmin(currentUser);

        int currentPage = PageUtils.normalizePage(page);
        int currentPageSize = PageUtils.normalizePageSize(pageSize);
        int offset = PageUtils.offset(currentPage, currentPageSize);

        List<User> records = userMapper.findPage(offset, currentPageSize);
        long total = userMapper.countAll();

        return PageResponse.of(records, total, currentPage, currentPageSize);
    }

    @Transactional
    public User update(Long id, UpdateUserRequest request) {
        User user = findById(id);

        User existingUser = userMapper.findByEmail(request.getEmail());

        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new BusinessException(409, "邮箱已存在");
        }

        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());

        userMapper.update(user);

        return findById(id);
    }

    @Transactional
    public void delete(Long id, AuthUser currentUser) {
        PermissionUtils.requireAdmin(currentUser);

        if (currentUser.getId().equals(id)) {
            throw new BusinessException(400, "不能删除自己");
        }

        findById(id);

        long articleCount = articleMapper.countByAuthorId(id);

        if (articleCount > 0) {
            throw new BusinessException(409, "该用户下还有文章，不能删除");
        }

        userMapper.softDeleteById(id);
    }
}