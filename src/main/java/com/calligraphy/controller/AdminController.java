package com.calligraphy.controller;

import com.calligraphy.common.Result;
import com.calligraphy.entity.Order;
import com.calligraphy.entity.Product;
import com.calligraphy.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public Result users() {
        return Result.success(adminService.listUsers());
    }

    @PostMapping("/user/ban/{id}")
    public Result banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return Result.success();
    }

    @GetMapping("/products")
    public Result products() {
        return Result.success(adminService.listProducts());
    }

    @PostMapping("/product/add")
    public Result addProduct(@RequestBody Product product) {
        adminService.addProduct(product);
        return Result.success();
    }

    @DeleteMapping("/product/delete/{id}")
    public Result deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return Result.success();
    }

    @GetMapping("/orders")
    public Result orders() {
        return Result.success(adminService.listOrders());
    }

    @PostMapping("/order/update")
    public Result updateOrder(@RequestBody Order order) {
        adminService.updateOrder(order);
        return Result.success();
    }

    @PostMapping("/user/role/{id}")
    public Result setUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        adminService.setUserRole(id, body.get("role"));
        return Result.success();
    }

    @PostMapping("/user/reset-password/{id}")
    public Result resetUserPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        adminService.resetUserPassword(id, body.get("password"));
        return Result.success();
    }
}
