package com.calligraphy.service.impl;

import com.calligraphy.common.enums.ResultCodeEnum;
import com.calligraphy.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.PageResult;
import com.calligraphy.dto.ContentDTO;
import com.calligraphy.dto.ContentVO;
import com.calligraphy.entity.Category;
import com.calligraphy.entity.Content;
import com.calligraphy.entity.ContentLike;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.CategoryMapper;
import com.calligraphy.mapper.ContentLikeMapper;
import com.calligraphy.mapper.ContentMapper;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentMapper contentMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final ContentLikeMapper contentLikeMapper;

    public ContentServiceImpl(ContentMapper contentMapper,
                              UserMapper userMapper,
                              CategoryMapper categoryMapper,
                              ContentLikeMapper contentLikeMapper) {
        this.contentMapper = contentMapper;
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.contentLikeMapper = contentLikeMapper;
    }

    @Override
    @Transactional
    public void publish(ContentDTO dto, Long userId) {
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setContent(dto.getContent());
        content.setCover(dto.getCover());
        content.setType(dto.getType());
        content.setCategoryId(dto.getCategoryId());
        content.setUserId(userId);
        content.setViewCount(0);
        content.setLikeCount(0);
        contentMapper.insert(content);
    }

    @Override
    public PageResult<ContentVO> page(Integer pageNum, Integer pageSize, String type, Long categoryId, Long currentUserId) {
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null && !type.isBlank(), Content::getType, type)
                .eq(categoryId != null, Content::getCategoryId, categoryId)
                .orderByDesc(Content::getCreateTime);

        Page<Content> page = new Page<>(pageNum, pageSize);
        Page<Content> result = contentMapper.selectPage(page, wrapper);

        List<ContentVO> voList = new ArrayList<>();
        for (Content content : result.getRecords()) {
            voList.add(buildVO(content, currentUserId));
        }
        return new PageResult<>(result.getTotal(), voList);
    }

    @Override
    @Transactional
    public ContentVO detail(Long id, Long currentUserId) {
        Content content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException(ResultCodeEnum.CONTENT_NOT_FOUND);
        }
        int currentViewCount = content.getViewCount() == null ? 0 : content.getViewCount();
        content.setViewCount(currentViewCount + 1);
        contentMapper.updateById(content);
        return buildVO(content, currentUserId);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Content content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException(ResultCodeEnum.CONTENT_NOT_FOUND);
        }
        if (!content.getUserId().equals(userId)) {
            throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        }
        contentMapper.deleteById(id);
    }

    private ContentVO buildVO(Content c, Long currentUserId) {
        ContentVO vo = new ContentVO();
        vo.setId(c.getId());
        vo.setTitle(c.getTitle());
        vo.setContent(c.getContent());
        vo.setCover(c.getCover());
        vo.setType(c.getType());
        vo.setUserId(c.getUserId());
        vo.setCategoryId(c.getCategoryId());
        vo.setViewCount(c.getViewCount());
        vo.setLikeCount(c.getLikeCount());
        vo.setCreateTime(c.getCreateTime());

        User user = userMapper.selectById(c.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }

        Category category = categoryMapper.selectById(c.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        boolean liked = false;
        if (currentUserId != null) {
            LambdaQueryWrapper<ContentLike> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ContentLike::getContentId, c.getId())
                    .eq(ContentLike::getUserId, currentUserId);
            liked = contentLikeMapper.selectCount(wrapper) > 0;
        }
        vo.setLiked(liked);
        return vo;
    }
}