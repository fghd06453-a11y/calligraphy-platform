package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.entity.Order;
import com.calligraphy.service.OrderService;
import com.calligraphy.util.LoginUserContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Result create(@RequestBody Map<String, Long> body) {
        Long userId = LoginUserContext.getCurrentUserId();

        if (userId == null) {
            return Result.fail("请先登录");
        }

        Long productId = body.get("productId");

        if (productId == null) {
            return Result.fail("商品ID不能为空");
        }

        orderService.create(userId, productId);
        return Result.success();
    }

    @GetMapping("/my")
    public Result my() {
        Long userId = LoginUserContext.getCurrentUserId();

        if (userId == null) {
            return Result.fail("请先登录");
        }

        List<Order> list = orderService.myOrders(userId);
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(orderService.list());
    }

    @PostMapping("/update")
    public Result update(@RequestBody Order order) {
        orderService.update(order);
        return Result.success();
    }
}