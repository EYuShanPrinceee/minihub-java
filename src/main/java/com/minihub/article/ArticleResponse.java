package com.minihub.article;

public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorNickname;
    private Long viewCount;
    private Long likeCount;
    private Long version;

    public ArticleResponse() {
    }

    public ArticleResponse(
            Long id,
            String title,
            String content,
            Long authorId,
            String authorNickname,
            Long viewCount,
            Long likeCount,
            Long version
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.version = version;
    }

    public static ArticleResponse from(Article article, String authorNickname) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthorId(),
                authorNickname,
                article.getViewCount(),
                article.getLikeCount(),
                article.getVersion()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}