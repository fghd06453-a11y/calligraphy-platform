import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping("/create")
    public String create(@RequestBody Order order) {
        orderService.create(order);
        return "success";
    }

    // 我的订单
    @GetMapping("/my/{userId}")
    public List<Order> my(@PathVariable Long userId) {
        return orderService.myOrders(userId);
    }

    // 管理员查看全部
    @GetMapping("/list")
    public List<Order> list() {
        return orderService.all();
    }

    // 修改状态
    @PostMapping("/update")
    public String update(@RequestBody Order order) {
        orderService.update(order);
        return "success";
    }
}