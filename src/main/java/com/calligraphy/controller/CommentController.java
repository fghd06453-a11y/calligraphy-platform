package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.dto.CommentDTO;
import com.calligraphy.dto.CommentVO;
import com.calligraphy.service.CommentService;
import com.calligraphy.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/publish")
    public Result<Void> publish(@Valid @RequestBody CommentDTO dto,
                                @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isTokenValid(token)) return Result.fail("请先登录");
        Long userId = jwtUtil.getUserId(token);
        commentService.publish(dto, userId);
        return Result.success();
    }

    @GetMapping("/list/{contentId}")
    public Result<List<CommentVO>> list(@PathVariable Long contentId) {
        return Result.success(commentService.listByContentId(contentId));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id,
                               @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isTokenValid(token)) return Result.fail("请先登录");
        Long userId = jwtUtil.getUserId(token);
        commentService.delete(id, userId);
        return Result.success();
    }
}
