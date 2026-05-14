package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.common.enums.ResultCodeEnum;
import com.calligraphy.entity.ContentLike;
import com.calligraphy.exception.BusinessException;
import com.calligraphy.mapper.ContentLikeMapper;
import com.calligraphy.mapper.ContentMapper;
import com.calligraphy.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    private final ContentLikeMapper contentLikeMapper;
    private final ContentMapper contentMapper;

    public LikeServiceImpl(ContentLikeMapper contentLikeMapper, ContentMapper contentMapper) {
        this.contentLikeMapper = contentLikeMapper;
        this.contentMapper = contentMapper;
    }

    @Override
    @Transactional
    public boolean toggleLike(Long contentId, Long userId) {
        if (contentMapper.selectById(contentId) == null) {
            throw new BusinessException(ResultCodeEnum.CONTENT_NOT_FOUND);
        }

        LambdaQueryWrapper<ContentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentLike::getContentId, contentId)
                .eq(ContentLike::getUserId, userId);

        ContentLike existing = contentLikeMapper.selectOne(wrapper);

        if (existing != null) {
            contentLikeMapper.deleteById(existing.getId());
            contentMapper.decrementLikeCount(contentId);
            return false;
        }

        ContentLike like = new ContentLike();
        like.setContentId(contentId);
        like.setUserId(userId);
        contentLikeMapper.insert(like);

        contentMapper.incrementLikeCount(contentId);
        return true;
    }
}
