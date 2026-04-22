package com.calligraphy.exception;

/**
 * 未授权异常（未登录 / token 无效）
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("未登录或登录已过期");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}