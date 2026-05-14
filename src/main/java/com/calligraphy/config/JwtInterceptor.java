package com.calligraphy.config;

import com.calligraphy.exception.UnauthorizedException;
import com.calligraphy.util.JwtUtil;
import com.calligraphy.util.LoginUserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || authHeader.isBlank()) {
            throw new UnauthorizedException("请求未携带 token");
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("token 格式错误");
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();

        if (!jwtUtil.isTokenValid(token)) {
            throw new UnauthorizedException("token 无效或已过期");
        }

        Long userId = jwtUtil.getUserId(token);

        if (userId == null) {
            throw new UnauthorizedException("无法解析当前登录用户");
        }

        String role = jwtUtil.getRole(token);

        LoginUserContext.setCurrentUserId(userId);
        LoginUserContext.setCurrentRole(role);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        LoginUserContext.clear();
    }
}