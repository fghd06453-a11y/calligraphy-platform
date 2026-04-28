package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.dto.UserUpdateDTO;
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
    public void register(RegisterDTO registerDTO) {

        if (registerDTO.getUsername() == null || registerDTO.getPassword() == null) {
            throw new BusinessException("用户名或密码不能为空");
        }

        // 查询用户是否存在
        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, registerDTO.getUsername())
        );

        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        // 构造用户（保持你原有风格）
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // 保持你原项目逻辑
        user.setNickname(
                registerDTO.getNickname() == null
                        ? registerDTO.getUsername()
                        : registerDTO.getNickname()
        );

        // ⚠️ 如果你数据库还没改，这两行先注释
        // user.setRole("user");
        // user.setStatus("正常");

        userMapper.insert(user);
    }

    @Override
    public void updateProfile(UserUpdateDTO dto, Long userId) {

        if (userId == null) {
            throw new BusinessException("未登录");
        }

        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新字段（按你现有字段来）
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());

        userMapper.updateById(user);
    }


    /**
     * 登录
     */
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
//从@Requestbody注解来的参数设置到loginDTO 后判断不能为空，
        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            throw new BusinessException("用户名或密码不能为空");
        }
//“：：”表示引用，引用user类的GETUSERNAME()方法，直接拿数据库里的username字段
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginDTO.getUsername())
        );

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
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


    /**
     * 修改信息
     */


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