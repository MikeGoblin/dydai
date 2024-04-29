package tech.revocat.daidai.service;

import com.alibaba.fastjson2.JSONObject;

import java.math.BigDecimal;

public interface OrderService {
    JSONObject getOrderList(Integer maxsum, Integer page, String openID);

    JSONObject getOrderInfo(Integer orderID);

    JSONObject getUserOrder(String openID, Integer status);

    JSONObject getOrderReceived(String openID);

    JSONObject createOrder(String openID, String title, String content,
                           BigDecimal value, String targetAddress,
                           String ordererAddress, String phone);

    JSONObject modifyOrder(String openID, Integer orderID, String title, String content,
                           BigDecimal value, String targetAddress,
                           String ordererAddress, String phone);


    JSONObject cancelOrder(String openID, Integer orderID);

    JSONObject checkOrder(String openID, Integer orderID);

    JSONObject receiveOrder(String openID, Integer orderID);

    JSONObject giveUpOrder(String openID, Integer orderID);

}
