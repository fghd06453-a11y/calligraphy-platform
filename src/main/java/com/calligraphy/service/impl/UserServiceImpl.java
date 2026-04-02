package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.UserService;
import com.calligraphy.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


//为什么登录成功返回 Map<String, Object>
//因为登录返回的数据通常是组合数据，不只是一个 token。
//例如前端常常需要同时拿到：
//token
//userId
//username
//nickname
//avatar
//所以当前用 Map 比较方便。
//如果想进一步规范，也可以单独建 LoginVO



//当前阶段
//先用明文密码，是为了把主流程跑通。
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);

        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername())
                .eq(User::getPassword, loginDTO.getPassword());

        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());

        return result;
    }
}
