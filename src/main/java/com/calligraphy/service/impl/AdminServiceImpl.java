package com.calligraphy.service.impl;

import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.AdminService;
import com.calligraphy.service.OrderService;
import com.calligraphy.service.ProductService;
import com.calligraphy.util.LoginUserHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final ProductService productService;
    private final OrderService orderService;
    private final LoginUserHelper loginUserHelper;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserMapper userMapper,
                            ProductService productService,
                            OrderService orderService,
                            LoginUserHelper loginUserHelper,
                            PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.productService = productService;
        this.orderService = orderService;
        this.loginUserHelper = loginUserHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listUsers() {
        loginUserHelper.getRequiredAdminUserId();
        return userMapper.selectList(null);
    }

    @Override
    public void banUser(Long id) {
        loginUserHelper.getRequiredAdminUserId();
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus("封禁");
        userMapper.updateById(user);
    }

    @Override
    public List<Product> listProducts() {
        loginUserHelper.getRequiredAdminUserId();
        return productService.list();
    }

    @Override
    public void addProduct(Product product) {
        loginUserHelper.getRequiredAdminUserId();
        productService.add(product);
    }

    @Override
    public void deleteProduct(Long id) {
        loginUserHelper.getRequiredAdminUserId();
        productService.delete(id);
    }

    @Override
    public List<Order> listOrders() {
        loginUserHelper.getRequiredAdminUserId();
        return orderService.list();
    }

    @Override
    public void updateOrder(Order order) {
        loginUserHelper.getRequiredAdminUserId();
        orderService.update(order);
    }

    @Override
    public void setUserRole(Long id, String role) {
        loginUserHelper.getRequiredAdminUserId();
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setRole(role);
        userMapper.updateById(user);
    }

    @Override
    public void resetUserPassword(Long id, String newPassword) {
        loginUserHelper.getRequiredAdminUserId();
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}
