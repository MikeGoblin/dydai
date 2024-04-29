package tech.revocat.daidai.entity.req;

import lombok.Data;

@Data
public class OrderListReq {
    private String token;
    private Integer maxnum; // 最大订单数量
    private Integer page; // 页数
}
