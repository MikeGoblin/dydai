package tech.revocat.daidai.service.impl;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.revocat.daidai.entity.Order;
import tech.revocat.daidai.entity.resp.OrderInfoResp;
import tech.revocat.daidai.mapper.CoinMapper;
import tech.revocat.daidai.mapper.OrderMapper;
import tech.revocat.daidai.service.OrderService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Resource
    OrderMapper mapper;

    @Resource
    CoinMapper coinMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public JSONObject getOrderList(Integer maxsum, Integer page, String openID) {
        System.out.println("openid"+openID);
        JSONObject object = new JSONObject();
        Integer offset = maxsum * page;
        List<Order> orderList = mapper.getOrderList(maxsum, openID, offset);
        if (orderList.isEmpty()) {
            object.put("success", 0);
            object.put("msg", "没有可接取的订单");
            return object;
        }
        object.put("success", 1);
        object.put("count", orderList.toArray().length);
        object.put("orders", orderList);
        return object;
    }

    @Override
    public JSONObject getOrderInfo(Integer orderID) {
        JSONObject object = new JSONObject();
        OrderInfoResp resp = mapper.getOrderInfo(orderID);
        if (resp == null) {
            object.put("success", 0);
            object.put("msg", "没有找到订单！");
            return object;
        }
        object.put("success", 1);
        object.put("title", resp.getTitle());
        object.put("content", resp.getContent());
        object.put("value", resp.getValue());
        object.put("target_address", resp.getTargetAddress());
        object.put("orderer_address", resp.getOrdererAddress());
        object.put("status", resp.getStatus());
        object.put("phone", resp.getPhone());

        Date time = resp.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = format.format(time);
        object.put("ordertime", formatTime);
        object.put("orderername", resp.getUsername());
        object.put("ordereravatar", resp.getAvatar());
        return object;
    }

    @Override
    public JSONObject getUserOrder(String openID, Integer status) {
        JSONObject object = new JSONObject();
        List<Order> orderList;
        orderList = mapper.getUserOrder(openID, status);
        if (orderList.isEmpty()) {
            object.put("success", 0);
            object.put("msg", "未找到对应订单");
            return object;
        }
        object.put("success", 1);
        object.put("count", orderList.toArray().length);
        object.put("orders", orderList);
        return object;
    }

    @Override
    public JSONObject getOrderReceived(String openID) {
        JSONObject object = new JSONObject();
        List<Order> orderList = mapper.getOrderReceived(openID);
        if (orderList.isEmpty()) {
            object.put("success", 0);
            object.put("msg", "没有找到已经接受的订单");
            return object;
        }
        object.put("success", 1);
        object.put("count", orderList.toArray().length);
        object.put("mytasks", orderList);
        return object;
    }

    @Override
    public JSONObject createOrder(String openID, String title, String content, BigDecimal value,
                                  String targetAddress, String ordererAddress, String phone) {
        JSONObject object = new JSONObject();
        Order order = new Order();
        try {
            mapper.updateBalance(openID, value.negate(), value, BigDecimal.valueOf(0.00));
        } catch (Exception e) {
            object.put("success", 0);
            object.put("msg", "余额不足！");
            return object;
        }
        int res = mapper.createOrder(openID, title, content, value, targetAddress, ordererAddress, phone, order);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "创建订单失败！");
            return object;
        }
        // 修改用户发布订单数量
        mapper.updatePostedCount(openID, 1);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("Info:" + openID))) {
            stringRedisTemplate.delete("Info:" + openID);
        }
        stringRedisTemplate.opsForHash().put("Info:" + openID, "address", ordererAddress);
        stringRedisTemplate.opsForHash().put("Info:" + openID, "phone", phone);
        coinMapper.createdCoinRecord(openID, "发布订单", value.negate(), Math.toIntExact(order.getId()));
        object.put("success", 1);
        object.put("orderid", order.getId());
        return object;
    }

    @Override
    public JSONObject modifyOrder(String openID, Integer orderID, String title, String content, BigDecimal value, String targetAddress, String ordererAddress, String phone) {
        JSONObject object = new JSONObject();
        OrderInfoResp order = mapper.getOrderInfo(orderID);
        if (order == null || order.getId() == 0 || !order.getOpenID().equals(openID)) {
            object.put("success", 0);
            object.put("msg", "找不到对应订单！");
            return object;
        }
        try {
            mapper.updateBalance(openID, order.getValue().subtract(value),
                    value.subtract(order.getValue()), BigDecimal.valueOf(0.00));
        } catch (Exception e) {
            object.put("success", 0);
            object.put("msg", "余额不足！");
            return object;
        }
        int res = mapper.modifyOrder(orderID, title, content, value, targetAddress, ordererAddress, phone);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "订单修改失败！");
            return object;
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("Info:" + openID))) {
            stringRedisTemplate.delete("Info:" + openID);
        }
        stringRedisTemplate.opsForHash().put("Info:" + openID, "address", ordererAddress);
        stringRedisTemplate.opsForHash().put("Info:" + openID, "phone", phone);
        coinMapper.modifyCoinRecord(value.negate(), orderID);
        object.put("success", 1);
        return object;
    }

    @Override
    public JSONObject cancelOrder(String openID, Integer orderID) {
        JSONObject object = new JSONObject();
        OrderInfoResp order = mapper.getOrderInfo(orderID);
        if (order == null || order.getId() == 0 || !order.getOpenID().equals(openID)) {
            object.put("success", 0);
            object.put("msg", "找不到对应订单！");
            return object;
        }
        if (order.getStatus()==2){
            object.put("success", 0);
            object.put("msg", "订单已被接取！");
            return object;
        }
        int res = mapper.cancelOrder(orderID);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "取消订单失败！");
            return object;
        }
        mapper.updateBalance(openID, order.getValue(), order.getValue().negate(), BigDecimal.valueOf(0.00));
        mapper.updatePostedCount(openID, -1);
        coinMapper.cancelCoinRecord(orderID);
        object.put("success", 1);
        return object;
    }

    @Override
    public JSONObject checkOrder(String openID, Integer orderID) {
        JSONObject object = new JSONObject();
        OrderInfoResp order = mapper.getOrderInfo(orderID);
        if (order == null || order.getId() == 0 || !order.getOpenID().equals(openID)) {
            object.put("success", 0);
            object.put("msg", "找不到对应订单！");
            return object;
        }
        if ((order.getStatus() != 2)) {
            object.put("success", 0);
            object.put("msg", "订单状态异常！");
            return object;
        }
        int res = mapper.checkOrder(orderID);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "验收订单失败！");
            return object;
        }
        try {
            mapper.updateBalance(order.getRiderID(), order.getValue(),
                    BigDecimal.valueOf(0.00), order.getValue());
        } catch (Exception e) {
            object.put("success", 0);
            object.put("msg", "余额不足！");
            return object;
        }
        // fixme
        mapper.updateCompletedCount(openID);
        coinMapper.createdCoinRecord(order.getRiderID(), "成功完成一单", order.getValue(), orderID);
        object.put("success", 1);
        return object;
    }

    @Override
    public JSONObject receiveOrder(String openID, Integer orderID) {
        JSONObject object = new JSONObject();
        OrderInfoResp order = mapper.getOrderInfo(orderID);
        if (order == null || order.getId() == 0 || order.getOpenID().equals(openID)) {
            object.put("success", 0);
            object.put("msg", "找不到对应订单！");
            return object;
        }
        if (order.getStatus() != 1) {
            object.put("success", 0);
            object.put("msg", "订单状态异常！");
            return object;
        }
        int res = mapper.receiveOrder(openID, orderID);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "订单接受失败！");
            return object;
        }
        mapper.updateAcceptedCount(openID, 1);
        object.put("success", 1);
        return object;
    }

    @Override
    public JSONObject giveUpOrder(String openID, Integer orderID) {
        JSONObject object = new JSONObject();
        OrderInfoResp order = mapper.getOrderInfo(orderID);
        if (order == null || order.getId() == 0 || order.getOpenID().equals(openID)) {
            object.put("success", 0);
            object.put("msg", "找不到对应订单！");
            return object;
        }
        if (order.getStatus() != 2) {
            object.put("success", 0);
            object.put("msg", "订单状态异常！");
            return object;
        }
        int res = mapper.giveUpOrder(orderID);
        if (res == 0) {
            object.put("success", 0);
            object.put("msg", "放弃订单失败！");
            return object;
        }
        object.put("success", 1);
        mapper.updateAcceptedCount(openID, -1);
        return object;
    }
}
