package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.entity.Order;
import com.calligraphy.service.OrderService;
import com.calligraphy.util.LoginUserHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final LoginUserHelper loginUserHelper;

    public OrderController(OrderService orderService, LoginUserHelper loginUserHelper) {
        this.orderService = orderService;
        this.loginUserHelper = loginUserHelper;
    }

    @PostMapping("/create")
    public Result<Void> create(@RequestBody Map<String, Long> body) {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        Long productId = body.get("productId");

        if (productId == null) {
            return Result.fail("商品ID不能为空");
        }

        orderService.create(userId, productId);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<List<Order>> my() {
        Long userId = loginUserHelper.getRequiredCurrentUserId();
        return Result.success(orderService.myOrders(userId));
    }

    @GetMapping("/my/{userId}")
    public Result<List<Order>> myByUserId(@PathVariable Long userId) {
        return Result.success(orderService.myOrders(userId));
    }

    @GetMapping("/list")
    public Result<List<Order>> list() {
        return Result.success(orderService.list());
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Order order) {
        orderService.update(order);
        return Result.success();
    }
}