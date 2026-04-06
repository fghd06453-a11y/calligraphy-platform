package com.calligraphy.dto;

public class RegisterDTO {

    private String username;
    private String password;
    private String nickname;
    private String avatar; // ✅ 加这一行

    // getter / setter

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {   // ✅ 必须有
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}