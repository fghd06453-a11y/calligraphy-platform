package com.calligraphy.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    public LoginDTO() {
   }
//spring boot 自带的JACKSON框架进行JSON序列化，自动调用DTO 把前端传来的参数给 set 方法，（这一步从CONTROLLER层的@Requestbody注解开始，LoginDTO 里需要有set方法）
//    同时这个框架还能将Java对象转成JSON给前端返回
//        使用@Requestbody注解，前端传参字段名需要与DTO字段名完全一致才能生效，或者使用别的注解
//        但是DTO的字段不需与实体类字段一致，实体类字段只需与数据库表字段一致，因为从DTO到
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
