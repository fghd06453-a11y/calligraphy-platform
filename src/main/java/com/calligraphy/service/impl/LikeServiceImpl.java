package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.entity.Content;
import com.calligraphy.entity.ContentLike;
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
        Content content = contentMapper.selectById(contentId);
        if (content == null) {
            throw new RuntimeException("内容不存在");
        }

        LambdaQueryWrapper<ContentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentLike::getContentId, contentId)
                .eq(ContentLike::getUserId, userId);

        ContentLike existing = contentLikeMapper.selectOne(wrapper);

        int currentLikeCount = content.getLikeCount() == null ? 0 : content.getLikeCount();

        if (existing != null) {
            contentLikeMapper.deleteById(existing.getId());
            content.setLikeCount(Math.max(0, currentLikeCount - 1));
            contentMapper.updateById(content);
            return false;
        }

        ContentLike like = new ContentLike();
        like.setContentId(contentId);
        like.setUserId(userId);
        contentLikeMapper.insert(like);

        content.setLikeCount(currentLikeCount + 1);
        contentMapper.updateById(content);
        return true;
    }
}
