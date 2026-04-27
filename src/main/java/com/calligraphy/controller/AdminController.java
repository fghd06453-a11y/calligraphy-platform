package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.service.OrderService;
import com.calligraphy.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final UserMapper userMapper;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(UserMapper userMapper,
                           ProductService productService,
                           OrderService orderService) {
        this.userMapper = userMapper;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    public Result users() {
        return Result.success(userMapper.selectList(null));
    }

    @PostMapping("/user/ban/{id}")
    public Result banUser(@PathVariable Long id) {
        var user = userMapper.selectById(id);
        if (user == null) return Result.fail("用户不存在");
        user.setStatus("封禁");
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping("/products")
    public Result products() {
        return Result.success(productService.list());
    }

    @PostMapping("/product/add")
    public Result addProduct(@RequestBody Product product) {
        productService.add(product);
        return Result.success();
    }

    @DeleteMapping("/product/delete/{id}")
    public Result deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }

    @GetMapping("/orders")
    public Result orders() {
        return Result.success(orderService.list());
    }

    @PostMapping("/order/update")
    public Result updateOrder(@RequestBody Order order) {
        orderService.update(order);
        return Result.success();
    }
}
