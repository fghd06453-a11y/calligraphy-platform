package com.calligraphy.service;

import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;

import java.util.Map;

public interface UserService {

    void register(RegisterDTO registerDTO);

    Map<String, Object> login(LoginDTO loginDTO);
}
