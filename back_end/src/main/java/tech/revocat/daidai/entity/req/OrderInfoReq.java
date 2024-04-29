package tech.revocat.daidai.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderInfoReq {
    private String token;
    @JsonProperty("orderid")
    private Integer orderID;
}
