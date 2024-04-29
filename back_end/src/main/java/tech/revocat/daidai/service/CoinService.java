package tech.revocat.daidai.service;

import com.alibaba.fastjson2.JSONObject;

public interface CoinService {
    JSONObject getCoinInfo(String openID);

    JSONObject getCoinRecords(String openID, Integer maxCount, Integer page);
}
