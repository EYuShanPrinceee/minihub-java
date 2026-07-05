package com.minihub.article;

import com.minihub.auth.AuthUser;
import com.minihub.auth.CurrentUser;
import com.minihub.common.ApiResponse;
import com.minihub.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ApiResponse<ArticleResponse> create(
            @CurrentUser AuthUser currentUser,
            @RequestBody @Valid CreateArticleRequest request
    ) {
        Article article = articleService.create(request, currentUser);
        return ApiResponse.success(articleService.toResponse(article), "创建文章成功");
    }

    @GetMapping("/{id}")
    public ApiResponse<ArticleResponse> findById(@PathVariable Long id) {
        ArticleResponse response = articleService.findDetailById(id);
        return ApiResponse.success(response, "查询文章成功");
    }

    @GetMapping
    public ApiResponse<PageResponse<ArticleResponse>> findPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        PageResponse<Article> pageData = articleService.findPage(
                keyword,
                authorId,
                title,
                page,
                pageSize
        );

        List<ArticleResponse> records = pageData.getRecords()
                .stream()
                .map(articleService::toResponse)
                .toList();

        PageResponse<ArticleResponse> result = PageResponse.of(
                records,
                pageData.getTotal(),
                pageData.getPage(),
                pageData.getPageSize()
        );

        return ApiResponse.success(result, "查询文章列表成功");
    }

    @PutMapping("/{id}")
    public ApiResponse<ArticleResponse> update(
            @CurrentUser AuthUser currentUser,
            @PathVariable Long id,
            @RequestBody @Valid UpdateArticleRequest request
    ) {
        Article article = articleService.update(id, request, currentUser);
        return ApiResponse.success(articleService.toResponse(article), "更新文章成功");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(
            @CurrentUser AuthUser currentUser,
            @PathVariable Long id
    ) {
        articleService.delete(id, currentUser);
        return ApiResponse.success(null, "删除文章成功");
    }

    @PostMapping("/{id}/view")
    public ApiResponse<ArticleResponse> view(@PathVariable Long id) {
        Article article = articleService.increaseViewCount(id);
        return ApiResponse.success(articleService.toResponse(article), "浏览成功");
    }

    @PostMapping("/{id}/like")
    public ApiResponse<ArticleResponse> like(@PathVariable Long id) {
        Article article = articleService.increaseLikeCount(id);
        return ApiResponse.success(articleService.toResponse(article), "点赞成功");
    }
}