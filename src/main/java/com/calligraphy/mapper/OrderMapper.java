@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders(user_id,product_id,price,status) VALUES(#{userId},#{productId},#{price},#{status})")
    void insert(Order order);

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> listByUser(Long userId);

    @Select("SELECT * FROM orders")
    List<Order> listAll();

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatus(Order order);
}