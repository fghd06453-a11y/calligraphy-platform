package com.calligraphy.service;

import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.dto.UserUpdateDTO;
import com.calligraphy.entity.User;

import java.util.Map;

public interface UserService {

    void register(RegisterDTO registerDTO);

    Map<String, Object> login(LoginDTO loginDTO);

    User getUserInfo(Long userId);

    void updateProfile(UserUpdateDTO dto, Long userId);

    void updatePassword(String oldPassword, String newPassword);
}