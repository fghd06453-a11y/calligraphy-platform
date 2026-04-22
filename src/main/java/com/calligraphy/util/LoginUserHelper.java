package com.calligraphy.util;

import org.springframework.stereotype.Component;

@Component
public class LoginUserHelper {

    private final JwtUtil jwtUtil;

    public LoginUserHelper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getCurrentUserId(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }

        String token = authorization;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        if (!jwtUtil.isTokenValid(token)) {
            return null;
        }

        return jwtUtil.getUserId(token);
    }

    public boolean isLogin(String authorization) {
        return getCurrentUserId(authorization) != null;
    }
}
