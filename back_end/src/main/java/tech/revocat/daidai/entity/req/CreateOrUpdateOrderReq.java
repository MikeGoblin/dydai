package tech.revocat.daidai.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrUpdateOrderReq {
    @JsonProperty("orderid")
    private Integer id;
    private String token;
    private String title;
    private String content;
    private String phone;
    @JsonProperty("target_address")
    private String targetAddress;
    @JsonProperty("orderer_address")
    private String ordererAddress;
    private BigDecimal value; // 交易额
}
