package com.minihub.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateArticleRequest {

    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 100, message = "标题长度必须在 2 到 100 个字符之间")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 10, message = "内容至少需要 10 个字符")
    private String content;

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
}