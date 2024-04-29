package tech.revocat.daidai.service.impl;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.revocat.daidai.entity.CoinRecord;
import tech.revocat.daidai.entity.resp.CoinInfoResp;
import tech.revocat.daidai.mapper.CoinMapper;
import tech.revocat.daidai.service.CoinService;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CoinServiceImpl implements CoinService {
    @Resource
    CoinMapper mapper;

    public JSONObject getCoinInfo(String openID) {
        JSONObject object = new JSONObject();
        CoinInfoResp coinInfoResp = mapper.getCoinInfo(openID);
        if (coinInfoResp == null) {
            object.put("success", 0);
            object.put("msg", "找不到记录");
            return object;
        }
        object.put("success", 1);
        object.put("coin", coinInfoResp.getBalance());
        object.put("coin_used", coinInfoResp.getCoinUsed());
        object.put("coin_gained", coinInfoResp.getCoinGained());
        return object;
    }

    @Override
    public JSONObject getCoinRecords(String openID, Integer maxCount, Integer page) {
        JSONObject object = new JSONObject();
        Integer offset = maxCount * page;
        List<CoinRecord> coinRecords = mapper.getCoinRecordList(openID, maxCount, offset);
        if (coinRecords == null) {
            object.put("success", 0);
            object.put("msg", "找不到记录");
            return object;
        }
        object.put("success", 1);
        object.put("orders", coinRecords);
        return object;
    }
}
