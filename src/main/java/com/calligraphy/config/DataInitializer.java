package com.calligraphy.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            Long adminCount = userMapper.selectCount(
                    new LambdaQueryWrapper<User>().eq(User::getRole, "admin")
            );

            if (adminCount == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNickname("系统管理员");
                admin.setRole("admin");
                admin.setStatus("正常");
                admin.setCreateTime(LocalDateTime.now());
                admin.setUpdateTime(LocalDateTime.now());
                userMapper.insert(admin);
                System.out.println("=== 默认管理员已创建: admin / admin123 ===");
            } else {
                System.out.println("=== 管理员账号已存在，跳过创建 ===");
            }
        } catch (Exception e) {
            System.out.println("=== 数据库表尚未初始化，跳过管理员创建。请执行 schema.sql ===");
        }
    }
}
