package com.minihub.article;

import com.minihub.auth.AuthUser;
import com.minihub.common.PageResponse;
import com.minihub.common.PageUtils;
import com.minihub.exception.BusinessException;
import com.minihub.exception.ErrorCode;
import com.minihub.user.User;
import com.minihub.user.UserMapper;
import com.minihub.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ArticleCacheService articleCacheService;

    public ArticleService(
            ArticleMapper articleMapper,
            UserService userService,
            UserMapper userMapper,
            ArticleCacheService articleCacheService
    ) {
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.articleCacheService = articleCacheService;
    }

    @Transactional
    public Article create(CreateArticleRequest request, AuthUser currentUser) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setAuthorId(currentUser.getId());

        articleMapper.insert(article);

        int affectedRows = userMapper.increaseArticleCount(currentUser.getId());

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在或已被删除");
        }

        return findById(article.getId());
    }

    public Article findById(Long id) {
        Article article = articleMapper.findById(id);

        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        return article;
    }

    public ArticleResponse findDetailById(Long id) {
        ArticleResponse cachedResponse = articleCacheService.getDetail(id);

        if (cachedResponse != null) {
            return cachedResponse;
        }

        Article article = findById(id);
        ArticleResponse response = toResponse(article);

        articleCacheService.setDetail(id, response);

        return response;
    }

    public PageResponse<Article> findPage(
            String keyword,
            Long authorId,
            String title,
            Integer page,
            Integer pageSize
    ) {
        int currentPage = PageUtils.normalizePage(page);
        int currentPageSize = PageUtils.normalizePageSize(pageSize);
        int offset = PageUtils.offset(currentPage, currentPageSize);

        String normalizedKeyword = normalizeText(keyword);
        String normalizedTitle = normalizeText(title);

        List<Article> records = articleMapper.findPage(
                normalizedKeyword,
                authorId,
                normalizedTitle,
                offset,
                currentPageSize
        );

        long total = articleMapper.count(
                normalizedKeyword,
                authorId,
                normalizedTitle
        );

        return PageResponse.of(records, total, currentPage, currentPageSize);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request, AuthUser currentUser) {
        Article article = findById(id);

        checkOwner(article, currentUser);

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setVersion(request.getVersion());

        int affectedRows = articleMapper.update(article);

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "数据已被修改，请刷新后重试");
        }

        articleCacheService.deleteDetail(id);

        return findById(id);
    }

    @Transactional
    public void delete(Long id, AuthUser currentUser) {
        Article article = findById(id);

        checkOwner(article, currentUser);

        int deletedRows = articleMapper.softDeleteById(id);

        if (deletedRows == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        int affectedRows = userMapper.decreaseArticleCount(article.getAuthorId());

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在或已被删除");
        }

        articleCacheService.deleteDetail(id);
    }

    @Transactional
    public Article increaseViewCount(Long id) {
        findById(id);

        int affectedRows = articleMapper.increaseViewCount(id);

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        articleCacheService.deleteDetail(id);

        return findById(id);
    }

    @Transactional
    public Article increaseLikeCount(Long id) {
        findById(id);

        int affectedRows = articleMapper.increaseLikeCount(id);

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        articleCacheService.deleteDetail(id);

        return findById(id);
    }

    public ArticleResponse toResponse(Article article) {
        User author = userService.findById(article.getAuthorId());
        return ArticleResponse.from(article, author.getNickname());
    }

    private void checkOwner(Article article, AuthUser currentUser) {
        if (!article.getAuthorId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作这篇文章");
        }
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}