package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.service.LikeService;
import com.calligraphy.util.LoginUserHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;
    private final LoginUserHelper loginUserHelper;

    public LikeController(LikeService likeService, LoginUserHelper loginUserHelper) {
        this.likeService = likeService;
        this.loginUserHelper = loginUserHelper;
    }

    @PostMapping("/toggle/{contentId}")
    public Result<Boolean> toggle(@PathVariable Long contentId,
                                  @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = loginUserHelper.getCurrentUserId(authorization);
        if (userId == null) {
            return Result.fail("请先登录");
        }
        return Result.success(likeService.toggleLike(contentId, userId));
    }
}