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

    public Long getRequiredAdminUserId() {
        Long userId = getRequiredCurrentUserId();
        String role = LoginUserContext.getCurrentRole();
        if (!"admin".equals(role)) {
            throw new UnauthorizedException("无管理员权限");
        }
        return userId;
    }
}
