package tech.revocat.daidai.mapper;

import org.apache.ibatis.annotations.*;
import tech.revocat.daidai.entity.CoinRecord;
import tech.revocat.daidai.entity.resp.CoinInfoResp;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CoinMapper {
    @Select("SELECT u.balance,u.coin_used,u.coin_gained FROM carry.user u WHERE open_id = #{open_id}")
    CoinInfoResp getCoinInfo(@Param("open_id") String openID);

    @Insert("INSERT INTO coin(user_id, info, value,order_id) VALUES (#{open_id},#{info},#{value},#{order_id})")
    void createdCoinRecord(@Param("open_id") String openID, @Param("info") String info,
                           @Param("value") BigDecimal value, @Param("order_id") Integer orderID);

    @Update("UPDATE coin SET value = #{value} WHERE order_id = #{order_id}")
    void modifyCoinRecord(@Param("value") BigDecimal value, @Param("order_id") Integer orderID);

    @Update("UPDATE coin SET is_cancelled = 1 WHERE order_id = #{order_id}")
    void cancelCoinRecord(@Param("order_id") Integer orderID);

    @Select("SELECT info,time,value FROM coin WHERE user_id = #{open_id} " +
            "AND is_cancelled != 1 ORDER BY id DESC LIMIT #{max_count} OFFSET #{offset}")
    List<CoinRecord> getCoinRecordList(@Param("open_id") String openID,
                                       @Param("max_count") Integer maxCount, @Param("offset") Integer offset);
}
