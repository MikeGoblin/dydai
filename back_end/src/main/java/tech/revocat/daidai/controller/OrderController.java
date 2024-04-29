package tech.revocat.daidai.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.revocat.daidai.entity.req.CreateOrUpdateOrderReq;
import tech.revocat.daidai.entity.req.OrderInfoReq;
import tech.revocat.daidai.entity.req.OrderListReq;
import tech.revocat.daidai.entity.req.UserOrderReq;
import tech.revocat.daidai.service.OrderService;
import tech.revocat.daidai.utils.JWTUtil;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService service;

    @PostMapping("/get-all")
    public JSONObject getAllOrders(@RequestBody OrderListReq orderListReq) {
        System.out.println(orderListReq);
        String openID = JWTUtil.decodeToken(orderListReq.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getOrderList(orderListReq.getMaxnum(), orderListReq.getPage(), openID);
    }

    @PostMapping("/get-info")
    public JSONObject getOrderInfo(@RequestBody OrderInfoReq orderInfoReq) {
        return service.getOrderInfo(orderInfoReq.getOrderID());
    }

    @PostMapping("/user-order")
    public JSONObject getUserOrder(@RequestBody UserOrderReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getUserOrder(openID, req.getStatus());
    }

    @PostMapping("/received")
    public JSONObject getOrderReceived(@RequestBody JSONObject json) {
        String openID = JWTUtil.decodeToken(json.getString("token"));
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getOrderReceived(openID);
    }

    @PostMapping("/create")
    public JSONObject createOrder(@RequestBody CreateOrUpdateOrderReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.createOrder(openID, req.getTitle(), req.getContent(), req.getValue(),
                req.getTargetAddress(), req.getOrdererAddress(), req.getPhone());
    }

    @PostMapping("/modify")
    public JSONObject modifyOrder(@RequestBody CreateOrUpdateOrderReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.modifyOrder(openID, req.getId(), req.getTitle(),
                req.getContent(), req.getValue(), req.getTargetAddress(),
                req.getOrdererAddress(), req.getPhone());
    }

    @PostMapping("/delete")
    public JSONObject cancelOrder(@RequestBody OrderInfoReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.cancelOrder(openID, req.getOrderID());
    }

    @PostMapping("/check")
    public JSONObject checkOrder(@RequestBody OrderInfoReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.checkOrder(openID, req.getOrderID());
    }

    @PostMapping("/receive")
    public JSONObject receiveOrder(@RequestBody OrderInfoReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.receiveOrder(openID, req.getOrderID());
    }

    @PostMapping("/giveup")
    public JSONObject giveUpOrder(@RequestBody OrderInfoReq req) {
        String openID = JWTUtil.decodeToken(req.getToken());
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.giveUpOrder(openID, req.getOrderID());
    }
}
