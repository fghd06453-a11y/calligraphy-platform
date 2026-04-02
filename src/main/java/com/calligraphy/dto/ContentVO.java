package com.calligraphy.dto;

import java.time.LocalDateTime;

public class ContentVO {
    private Long id;
    private String title;
    private String content;
    private String cover;
    private String type;
    private Long userId;
    private String nickname;
    private String avatar;
    private Long categoryId;
    private String categoryName;
    private Integer viewCount;
    private Integer likeCount;
    private Boolean liked;
    private LocalDateTime createTime;

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

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}