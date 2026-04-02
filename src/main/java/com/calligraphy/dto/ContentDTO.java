package com.calligraphy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContentDTO {

    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String content;
    private String cover;

    @NotBlank(message = "类型不能为空")
    private String type;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
