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
//@RequestBody 注解表示检测到前端传来的JSON数据，然后注入到LoginDTO类里面，同时新建一个该对象，
//    再将这个对象的数据对应到login方法里面，然后在对应service实现类里运用
//  @Valid  这个注解用来检验前端传来的参数格式是否为空，长度等格式是否合格，简化service层面代码，不用判断数据是否合法
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