package com.calligraphy.service;

import com.calligraphy.entity.Order;

import java.util.List;

public interface OrderService {

    void create(Long userId, Long productId);

    List<Order> myOrders(Long userId);

    List<Order> list();

    void update(Order order);
}