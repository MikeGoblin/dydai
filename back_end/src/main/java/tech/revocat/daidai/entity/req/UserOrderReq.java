package tech.revocat.daidai.entity.req;

import lombok.Data;

@Data
public class UserOrderReq {
    private String token;
    private Integer status;
}
