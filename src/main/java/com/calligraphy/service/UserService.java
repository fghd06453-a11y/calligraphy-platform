package com.calligraphy.service;

import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.dto.UserUpdateDTO;

import java.util.Map;

public interface UserService {

    void register(RegisterDTO registerDTO);

    void updateProfile(UserUpdateDTO dto, Long userId);

    Map<String, Object> login(LoginDTO loginDTO);
}
