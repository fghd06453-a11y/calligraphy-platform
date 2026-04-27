@Data
public class Order {
    private Long id;
    private Long userId;
    private Long productId;
    private Double price;
    private String status;
    private Date createTime;
}