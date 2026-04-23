package com.calligraphy.util;

import com.calligraphy.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserHelper {

    public Long getCurrentUserId() {
        return LoginUserContext.getCurrentUserId();
    }

    public Long getRequiredCurrentUserId() {
        Long userId = LoginUserContext.getCurrentUserId();
        if (userId == null) {
            throw new UnauthorizedException("未登录或登录已过期");
        }
        return userId;
    }
}