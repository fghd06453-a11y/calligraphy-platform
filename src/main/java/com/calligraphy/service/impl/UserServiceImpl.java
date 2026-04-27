package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     * 注册
     */
    @Override
    public void register(User user) {
        // 1. 参数校验
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new BusinessException("用户名或密码不能为空");
        }

        // 2. 用户是否存在
        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, user.getUsername())
        );
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        // 3. 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4. 默认字段
        if (user.getNickname() == null) {
            user.setNickname(user.getUsername());
        }
        user.setRole("user");
        user.setStatus("正常");

        // 5. 插入
        userMapper.insert(user);
    }

    /**
     * 登录
     */
    @Override
    public Map<String, Object> login(String username, String password) {

        // 1. 查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 校验状态（封禁）
        if ("封禁".equals(user.getStatus())) {
            throw new BusinessException("账号已被封禁");
        }

        // 4. 生成 token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        // 5. 返回数据
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
     * 获取当前用户信息
     */
    @Override
    public User getCurrentUser() {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        return userMapper.selectById(userId);
    }

    /**
     * 更新用户信息
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