package com.calligraphy.controller;

import com.calligraphy.common.PageResult;
import com.calligraphy.common.Result;
import com.calligraphy.dto.ContentDTO;
import com.calligraphy.dto.ContentVO;
import com.calligraphy.service.ContentService;
import com.calligraphy.util.LoginUserHelper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;
    private final LoginUserHelper loginUserHelper;

    public ContentController(ContentService contentService, LoginUserHelper loginUserHelper) {
        this.contentService = contentService;
        this.loginUserHelper = loginUserHelper;
    }

    @PostMapping("/publish")
    public Result<Void> publish(@Valid @RequestBody ContentDTO dto) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        contentService.publish(dto, userId);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult<ContentVO>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) String type,
                                              @RequestParam(required = false) Long categoryId) {
        Long currentUserId = loginUserHelper.getCurrentUserId();
        return Result.success(contentService.page(pageNum, pageSize, type, categoryId, currentUserId));
    }

    @GetMapping("/detail/{id}")
    public Result<ContentVO> detail(@PathVariable Long id) {
        Long currentUserId = loginUserHelper.getCurrentUserId();
        return Result.success(contentService.detail(id, currentUserId));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        contentService.delete(id, userId);
        return Result.success();
    }
}