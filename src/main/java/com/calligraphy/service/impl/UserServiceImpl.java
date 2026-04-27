package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.entity.User;
import com.calligraphy.exception.BusinessException;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.UserService;
import com.calligraphy.util.JwtUtil;
import com.calligraphy.util.LoginUserHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final LoginUserHelper loginUserHelper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserMapper userMapper,
                           JwtUtil jwtUtil,
                           LoginUserHelper loginUserHelper) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.loginUserHelper = loginUserHelper;
    }

    /**
     * 注册（修复版本：使用DTO）
     */
    @Override
    public void register(RegisterDTO dto) {

        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new BusinessException("用户名或密码不能为空");
        }

        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, dto.getUsername())
        );

        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setNickname(
                dto.getNickname() == null ? dto.getUsername() : dto.getNickname()
        );

        user.setRole("user");
        user.setStatus("正常");

        userMapper.insert(user);
    }

    /**
     * 登录
     */
    @Override
    public Map<String, Object> login(String username, String password) {

        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if ("封禁".equals(user.getStatus())) {
            throw new BusinessException("账号已被封禁");
        }

        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("role", user.getRole());
        result.put("status", user.getStatus());

        return result;
    }

    /**
     * 当前用户
     */
    @Override
    public User getCurrentUser() {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        return userMapper.selectById(userId);
    }

    /**
     * 修改信息
     */
    @Override
    public void update(User user) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();

        User update = new User();
        update.setId(userId);
        update.setNickname(user.getNickname());
        update.setAvatar(user.getAvatar());

        userMapper.updateById(update);
    }

    /**
     * 修改密码
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {

        Long userId = loginUserHelper.getRequiredCurrentUserId();
        User user = userMapper.selectById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}