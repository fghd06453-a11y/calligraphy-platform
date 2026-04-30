package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.dto.LoginDTO;
import com.calligraphy.dto.RegisterDTO;
import com.calligraphy.dto.UserUpdateDTO;
import com.calligraphy.entity.User;
import com.calligraphy.service.UserService;
import com.calligraphy.util.LoginUserHelper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final LoginUserHelper loginUserHelper;

    public UserController(UserService userService, LoginUserHelper loginUserHelper) {
        this.userService = userService;
        this.loginUserHelper = loginUserHelper;
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

    @GetMapping("/me")
    public Result<User> me() {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UserUpdateDTO dto) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        userService.updateProfile(dto, userId);
        return Result.success();
    }

    // 兼容你原来的前端 /user/update
    @PostMapping("/update")
    public Result<Void> update(@RequestBody UserUpdateDTO dto) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        userService.updateProfile(dto, userId);
        return Result.success();
    }
}