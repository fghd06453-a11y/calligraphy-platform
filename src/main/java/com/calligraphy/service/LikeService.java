package com.calligraphy.service;

public interface LikeService {
    boolean toggleLike(Long contentId, Long userId);
}