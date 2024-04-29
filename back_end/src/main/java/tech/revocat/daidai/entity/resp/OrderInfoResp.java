package tech.revocat.daidai.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoResp {
    private Long id;
    private String title;
    private String content;
    private String phone;
    @JsonProperty(value = "target_address")
    private String targetAddress;
    @JsonProperty(value = "orderer_address")
    private String ordererAddress;
    private String username;
    private String avatar;
    private String openID;
    private String riderID;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private BigDecimal value; // 交易额
    //    private boolean isCompleted; // 交易是否完成
//    private boolean isCancelled; // 交易是否取消
//    private boolean isGoing; // 是否已经被接取
    private Integer status; // 订单状态。具体含义如下：
    // 1：订单可用（未被接受、未完成、未被取消）
    // 2：订单已被接受但未完成
    // 3：订单已完成
    // 4：订单已取消
    // 5：订单异常
}
