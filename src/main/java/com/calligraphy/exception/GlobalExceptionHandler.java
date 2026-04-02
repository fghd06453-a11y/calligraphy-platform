package com.calligraphy.exception;

import com.calligraphy.common.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntime(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        return Result.fail(msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.fail("服务器内部异常：" + e.getMessage());
    }
}

//异常处理返回数据格式：{
//  "code": 500,
//  "message": "用户名已存在",
//  "data": null
//}