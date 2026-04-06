package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.service.LikeService;
import com.calligraphy.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    public LikeController(LikeService likeService, JwtUtil jwtUtil) {
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/toggle/{contentId}")
    public Result<Boolean> toggle(@PathVariable Long contentId,
                                  @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getCurrentUserId(authorization);
        if (userId == null) {
            return Result.fail("请先登录");
        }
        return Result.success(likeService.toggleLike(contentId, userId));
    }

    private Long getCurrentUserId(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }

        String token = authorization;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        if (!jwtUtil.isTokenValid(token)) {
            return null;
        }

        return jwtUtil.getUserId(token);
    }
}