package tech.revocat.daidai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 交易记录
public class Order {
    private Long id;

    private String title;
    private String content;
    private String phone;
    @JsonProperty(value = "target_address")
    private String targetAddress;
    @JsonProperty(value = "orderer_address")
    private String ordererAddress;

    private User buyer; // 买方
    private User rider; // 骑手
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "ordertime")
    private Date time; // 交易时间
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
    private String failReason; // 交易失败原因
}
