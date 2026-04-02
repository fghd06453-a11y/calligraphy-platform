package com.calligraphy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {

    @NotNull(message = "内容ID不能为空")
    private Long contentId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
