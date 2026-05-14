package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.dto.CommentDTO;
import com.calligraphy.dto.CommentVO;
import com.calligraphy.entity.Comment;
import com.calligraphy.entity.User;
import com.calligraphy.exception.BusinessException;
import com.calligraphy.mapper.CommentMapper;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.CommentService;
import com.calligraphy.util.LoginUserContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    public CommentServiceImpl(CommentMapper commentMapper, UserMapper userMapper) {
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void publish(CommentDTO dto, Long userId) {
        Comment comment = new Comment();
        comment.setContentId(dto.getContentId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        commentMapper.insert(comment);
    }

    @Override
    public List<CommentVO> listByContentId(Long contentId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getContentId, contentId)
                .orderByDesc(Comment::getCreateTime);

        List<Comment> comments = commentMapper.selectList(wrapper);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        // Batch query users
        Set<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());
        Map<Long, User> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        return comments.stream().map(c -> {
            CommentVO vo = new CommentVO();
            vo.setId(c.getId());
            vo.setContentId(c.getContentId());
            vo.setUserId(c.getUserId());
            vo.setContent(c.getContent());
            vo.setCreateTime(c.getCreateTime());

            User user = userMap.get(c.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id, Long userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(userId) && !"admin".equals(LoginUserContext.getCurrentRole())) {
            throw new BusinessException("无权删除");
        }
        commentMapper.deleteById(id);
    }
}
