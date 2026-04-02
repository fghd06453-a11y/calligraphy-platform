package com.calligraphy.service;

import com.calligraphy.dto.CommentDTO;
import com.calligraphy.dto.CommentVO;
import java.util.List;

public interface CommentService {
    void publish(CommentDTO dto, Long userId);
    List<CommentVO> listByContentId(Long contentId);
    void delete(Long id, Long userId);
}