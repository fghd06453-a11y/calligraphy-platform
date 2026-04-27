package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.exception.BusinessException;
import com.calligraphy.mapper.OrderMapper;
import com.calligraphy.mapper.ProductMapper;
import com.calligraphy.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    @Override
    public void create(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(500, "商品不存在");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setPrice(product.getPrice());
        order.setStatus("未支付");

        orderMapper.insert(order);
    }

    @Override
    public List<Order> myOrders(Long userId) {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId, userId)
                        .orderByDesc(Order::getCreateTime)
        );
    }

    @Override
    public List<Order> list() {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .orderByDesc(Order::getCreateTime)
        );
    }

    @Override
    public void update(Order order) {
        Order update = new Order();
        update.setId(order.getId());
        update.setStatus(order.getStatus());
        orderMapper.updateById(update);
    }
}