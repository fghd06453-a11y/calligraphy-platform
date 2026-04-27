@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public void create(Order order) {
        order.setStatus("未支付");
        orderMapper.insert(order);
    }

    public List<Order> myOrders(Long userId) {
        return orderMapper.listByUser(userId);
    }

    public List<Order> all() {
        return orderMapper.listAll();
    }

    public void update(Order order) {
        orderMapper.updateStatus(order);
    }
}
