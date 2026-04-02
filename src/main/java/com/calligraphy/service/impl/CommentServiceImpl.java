package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.dto.CommentDTO;
import com.calligraphy.dto.CommentVO;
import com.calligraphy.entity.Comment;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.CommentMapper;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<CommentVO> result = new ArrayList<>();

        for (Comment c : comments) {
            CommentVO vo = new CommentVO();
            vo.setId(c.getId());
            vo.setContentId(c.getContentId());
            vo.setUserId(c.getUserId());
            vo.setContent(c.getContent());
            vo.setCreateTime(c.getCreateTime());

            User user = userMapper.selectById(c.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public void delete(Long id, Long userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除");
        }
        commentMapper.deleteById(id);
    }
}