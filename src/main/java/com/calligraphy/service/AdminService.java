package com.calligraphy.service;

import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.entity.User;

import java.util.List;

public interface AdminService {

    List<User> listUsers();

    void banUser(Long id);

    List<Product> listProducts();

    void addProduct(Product product);

    void deleteProduct(Long id);

    List<Order> listOrders();

    void updateOrder(Order order);

    void setUserRole(Long id, String role);

    void resetUserPassword(Long id, String newPassword);
}
