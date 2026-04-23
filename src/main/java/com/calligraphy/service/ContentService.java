package com.calligraphy.service;

import com.calligraphy.common.PageResult;
import com.calligraphy.dto.ContentDTO;
import com.calligraphy.dto.ContentVO;

public interface ContentService {

    void publish(ContentDTO dto, Long userId);

    PageResult<ContentVO> page(Integer pageNum, Integer pageSize, String type, Long categoryId, Long currentUserId);

    ContentVO detail(Long id, Long currentUserId);

    void delete(Long id, Long userId);
}