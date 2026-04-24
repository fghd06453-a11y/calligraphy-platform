package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.dto.UserUpdateDTO;
import com.calligraphy.service.UserService;
import com.calligraphy.util.LoginUserHelper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginDTO loginDTO) {
        Map<String, Object> data = userService.login(loginDTO);
        return Result.success(data);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody UserUpdateDTO dto) {
        LoginUserHelper loginUserHelper = new LoginUserHelper();
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        userService.updateProfile(dto, userId);
        return Result.success();
    }

}