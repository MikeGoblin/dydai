package tech.revocat.daidai.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 272657957766050828L;
    private String username;

    private String code;  //只是为了能接收参数，不需要存入数据库
    private String openID; //微信登录接口返回的参数之一，就是token
    private String token;

    private String avatar;

    private String address;
    private String phone;
    private Integer completedOrderCount;
    private Integer acceptedOrderCount;
    private Integer postedOrderCount;
    private Double balance;
    private List<Order> transactions; // 交易记录
}

