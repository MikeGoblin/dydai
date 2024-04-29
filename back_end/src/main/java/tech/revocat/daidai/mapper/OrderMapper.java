package tech.revocat.daidai.mapper;

import org.apache.ibatis.annotations.*;
import tech.revocat.daidai.entity.Order;
import tech.revocat.daidai.entity.resp.OrderInfoResp;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM `order` WHERE status = 1 AND `order`.buyer_id != #{open_id} " +
            "ORDER BY id DESC LIMIT #{maxnum} OFFSET #{offset}")
    List<Order> getOrderList(@Param("maxnum") Integer maxnum, @Param("open_id") String openID,
                             @Param("offset") Integer offset);

    @Select("SELECT o.id, o.title,o.content,o.value,o.target_address, o.time," +
            "o.orderer_address,o.status,o.phone, o.rider_id, u.username,u.avatar, u.open_id " +
            "FROM carry.`order` o " +
            "LEFT JOIN carry.user u ON o.buyer_id = u.open_id WHERE o.id = #{order_id}")
    OrderInfoResp getOrderInfo(@Param("order_id") Integer orderID);

    @Select("SELECT ID, TITLE, CONTENT, PHONE, TARGET_ADDRESS, orderer_ADDRESS, " +
            "STATUS, VALUE ,time FROM `order` WHERE buyer_id = #{open_id} AND IF(#{status} = 0, true, status = #{status})")
    List<Order> getUserOrder(@Param("open_id") String openID, @Param("status") Integer status);


    @Select("SELECT id, title, content,value ,status, phone, target_address, " +
            "orderer_address,time FROM `order` WHERE rider_id = #{open_id}")
    List<Order> getOrderReceived(@Param("open_id") String openID);

    @Insert("INSERT INTO `order`(buyer_id,title,content,value,target_address,orderer_address,phone) " +
            "VALUES (#{open_id},#{title},#{content},#{value},#{target_address},#{orderer_address},#{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "order.id", keyColumn = "id")
    int createOrder(@Param("open_id") String openID,
                    @Param("title") String title,
                    @Param("content") String content,
                    @Param("value") BigDecimal value,
                    @Param("target_address") String targetAddress,
                    @Param("orderer_address") String ordererAddress,
                    @Param("phone") String phone,
                    @Param("order") Order order);

    @Update("UPDATE `order` set title = #{title}, " +
            "content = #{content}, value = #{value}, target_address = #{target_address}, " +
            "orderer_address = #{orderer_address}, phone = #{phone} WHERE id = #{id}")
    int modifyOrder(
            @Param("id") Integer ID,
            @Param("title") String title,
            @Param("content") String content,
            @Param("value") BigDecimal value,
            @Param("target_address") String targetAddress,
            @Param("orderer_address") String ordererAddress,
            @Param("phone") String phone
    );

    @Update("UPDATE `order` SET status = 4 WHERE id = #{id}")
    int cancelOrder(@Param("id") Integer ID);

    @Update("UPDATE `order` SET status = 3 WHERE id = #{id}")
    int checkOrder(@Param("id") Integer ID);

    @Update("UPDATE carry.`order` t SET t.rider_id = #{open_id}, " +
            "t.status = 2 WHERE t.id = #{order_id}")
    int receiveOrder(@Param("open_id") String openID, @Param("order_id") Integer orderID);

    @Update("UPDATE carry.`order` t SET t.rider_id = null, " +
            "t.status = 1 WHERE t.id = #{order_id}")
    int giveUpOrder(@Param("order_id") Integer orderID);

    @Update("UPDATE user set user.balance = balance + #{value} ,user.coin_used = coin_used+#{coin_used}, " +
            "coin_gained = coin_gained + #{coin_gained} WHERE user.open_id = #{open_id}")
    void updateBalance(@Param("open_id") String openID, @Param("value") BigDecimal value,
                       @Param("coin_used") BigDecimal coinUsed, @Param("coin_gained") BigDecimal coinGained);

    @Update("UPDATE user SET user.accepted_order_count = accepted_order_count + #{count}")
    void updateAcceptedCount(@Param("open_id") String openID, @Param("count") Integer count);

    @Update("UPDATE user SET user.completed_order_count = completed_order_count + 1")
    void updateCompletedCount(@Param("open_id") String openID);

    @Update("UPDATE user SET user.posted_order_count = posted_order_count + #{count}")
    void updatePostedCount(@Param("open_id") String openID, @Param("count") Integer count);
}
