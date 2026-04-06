package com.calligraphy.controller;

import com.calligraphy.common.PageResult;
import com.calligraphy.common.Result;
import com.calligraphy.dto.ContentDTO;
import com.calligraphy.dto.ContentVO;
import com.calligraphy.service.ContentService;
import com.calligraphy.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;
    private final JwtUtil jwtUtil;

    public ContentController(ContentService contentService, JwtUtil jwtUtil) {
        this.contentService = contentService;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/publish")
    public Result<Void> publish(@Valid @RequestBody ContentDTO dto,
                                @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getCurrentUserId(authorization);
        if (userId == null) {
            return Result.fail("请先登录");
        }
        contentService.publish(dto, userId);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult<ContentVO>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) String type,
                                              @RequestParam(required = false) Long categoryId,
                                              @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long currentUserId = getCurrentUserId(authorization);
        return Result.success(contentService.page(pageNum, pageSize, type, categoryId, currentUserId));
    }

    @GetMapping("/detail/{id}")
    public Result<ContentVO> detail(@PathVariable Long id,
                                    @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long currentUserId = getCurrentUserId(authorization);
        return Result.success(contentService.detail(id, currentUserId));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id,
                               @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getCurrentUserId(authorization);
        if (userId == null) {
            return Result.fail("请先登录");
        }
        contentService.delete(id, userId);
        return Result.success();
    }
}