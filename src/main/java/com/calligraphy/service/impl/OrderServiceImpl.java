package com.calligraphy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.exception.BusinessException;
import com.calligraphy.mapper.OrderMapper;
import com.calligraphy.mapper.ProductMapper;
import com.calligraphy.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (userId == null) {
            throw new BusinessException("未登录");
        }

        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }

        Product product = productMapper.selectById(productId);

        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setPrice(product.getPrice());
        order.setStatus("未支付");
        order.setCreateTime(LocalDateTime.now());

        orderMapper.insert(order);
    }

    @Override
    public List<Order> myOrders(Long userId) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }

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
        if (order == null || order.getId() == null) {
            throw new BusinessException("订单ID不能为空");
        }

        Order update = new Order();
        update.setId(order.getId());
        update.setStatus(order.getStatus());

        orderMapper.updateById(update);
    }
}