package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.entity.ContentLike;

public interface ContentLikeMapper {
    void deleteById(Long id);

    void insert(ContentLike like);

    ContentLike selectOne(LambdaQueryWrapper<ContentLike> wrapper);

    int selectCount(LambdaQueryWrapper<ContentLike> wrapper);
}
