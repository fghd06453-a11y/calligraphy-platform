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
import com.calligraphy.util.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

        List<ContentVO> voList = buildVOList(result.getRecords(), currentUserId);
        return new PageResult<>(result.getTotal(), voList);
    }

    @Override
    @Transactional
    public ContentVO detail(Long id, Long currentUserId) {
        Content content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException(ResultCodeEnum.CONTENT_NOT_FOUND);
        }
        contentMapper.incrementViewCount(id);
        // refresh local snapshot for the return VO
        content.setViewCount(content.getViewCount() == null ? 1 : content.getViewCount() + 1);
        return buildVO(content, currentUserId);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Content content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException(ResultCodeEnum.CONTENT_NOT_FOUND);
        }
        if (!content.getUserId().equals(userId) && !"admin".equals(LoginUserContext.getCurrentRole())) {
            throw new BusinessException(ResultCodeEnum.NO_PERMISSION);
        }
        contentMapper.deleteById(id);
    }

    private List<ContentVO> buildVOList(List<Content> contentList, Long currentUserId) {
        if (contentList.isEmpty()) {
            return Collections.emptyList();
        }

        // Batch query users
        Set<Long> userIds = contentList.stream()
                .map(Content::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        final Map<Long, User> userMap = userIds.isEmpty()
                ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, u -> u));

        // Batch query categories
        Set<Long> categoryIds = contentList.stream()
                .map(Content::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        final Map<Long, Category> categoryMap = categoryIds.isEmpty()
                ? Collections.emptyMap()
                : categoryMapper.selectBatchIds(categoryIds).stream()
                        .collect(Collectors.toMap(Category::getId, c -> c));

        // Batch query likes for current user
        final Set<Long> likedContentIds;
        if (currentUserId != null) {
            LambdaQueryWrapper<ContentLike> likeWrapper = new LambdaQueryWrapper<>();
            likeWrapper.eq(ContentLike::getUserId, currentUserId)
                    .in(ContentLike::getContentId,
                            contentList.stream().map(Content::getId).collect(Collectors.toList()));
            likedContentIds = contentLikeMapper.selectList(likeWrapper).stream()
                    .map(ContentLike::getContentId)
                    .collect(Collectors.toSet());
        } else {
            likedContentIds = Collections.emptySet();
        }

        return contentList.stream().map(c -> {
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

            User user = userMap.get(c.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }

            Category category = categoryMap.get(c.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }

            vo.setLiked(likedContentIds.contains(c.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    private ContentVO buildVO(Content c, Long currentUserId) {
        return buildVOList(Collections.singletonList(c), currentUserId).get(0);
    }
}
