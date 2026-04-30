package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.dto.CommentDTO;
import com.calligraphy.dto.CommentVO;
import com.calligraphy.service.CommentService;
import com.calligraphy.util.LoginUserHelper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final LoginUserHelper loginUserHelper;

    public CommentController(CommentService commentService, LoginUserHelper loginUserHelper) {
        this.commentService = commentService;
        this.loginUserHelper = loginUserHelper;
    }

    @PostMapping("/publish")
    public Result<Void> publish(@Valid @RequestBody CommentDTO dto) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        commentService.publish(dto, userId);
        return Result.success();
    }

    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody CommentDTO dto) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        commentService.publish(dto, userId);
        return Result.success();
    }

    @GetMapping("/list/{contentId}")
    public Result<List<CommentVO>> list(@PathVariable Long contentId) {
        return Result.success(commentService.listByContentId(contentId));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        commentService.delete(id, userId);
        return Result.success();
    }
}