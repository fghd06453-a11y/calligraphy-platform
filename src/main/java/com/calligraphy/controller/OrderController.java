package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.entity.Order;
import com.calligraphy.service.OrderService;
import com.calligraphy.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    private Long getCurrentUserId(String token) {
        if (token == null || token.isBlank()) return null;
        if (!jwtUtil.isTokenValid(token)) return null;
        return jwtUtil.getUserId(token);
    }

    @PostMapping("/create")
    public Result create(@RequestBody Map<String, Long> body,
                         @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        if (userId == null) return Result.fail("请先登录");

        Long productId = body.get("productId");
        if (productId == null) return Result.fail("商品ID不能为空");

        orderService.create(userId, productId);
        return Result.success();
    }

    @GetMapping("/my")
    public Result my(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        if (userId == null) return Result.fail("请先登录");

        return Result.success(orderService.myOrders(userId));
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