package com.calligraphy.util;

import com.calligraphy.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserHelper {
//不需要声明LoginUserContext，因为getCurrentUserId()在LoginUserContext类里是静态方法，类名加点方法名就可以调用

    public Long getCurrentUserId() {
        return LoginUserContext.getCurrentUserId();
    }
    //    其次，LoginUserContext是底层，被LoginUserHelper封装，LoginUserHelper进行数据过滤，有个数据判断，符合要求才给用户ID
    public Long getRequiredCurrentUserId() {
        Long userId = LoginUserContext.getCurrentUserId();
        if (userId == null) {
            throw new UnauthorizedException("未登录或登录已过期");
        }
        return userId;
    }
}