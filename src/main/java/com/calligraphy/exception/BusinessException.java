package com.calligraphy.exception;

import com.calligraphy.common.enums.ResultCodeEnum;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    /**
     * 仅传消息，默认按 400 业务错误处理
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
        this.message = message;
    }

    /**
     * 传错误码和错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 传统一状态码枚举
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}