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
                                  @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isTokenValid(token)) return Result.fail("请先登录");
        Long userId = jwtUtil.getUserId(token);
        return Result.success(likeService.toggleLike(contentId, userId));
    }
}
