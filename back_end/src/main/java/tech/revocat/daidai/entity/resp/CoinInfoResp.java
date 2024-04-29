package tech.revocat.daidai.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinInfoResp {
    private BigDecimal balance;
    private BigDecimal coinUsed;
    private BigDecimal coinGained;
}
