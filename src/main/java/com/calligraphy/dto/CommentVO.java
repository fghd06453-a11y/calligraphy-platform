package com.calligraphy.dto;

import java.time.LocalDateTime;

public class CommentVO {
    private Long id;
    private Long contentId;
    private Long userId;
    private String nickname;
    private String avatar;
    private String content;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
