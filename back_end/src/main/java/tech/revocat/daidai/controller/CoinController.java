package tech.revocat.daidai.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.revocat.daidai.service.CoinService;
import tech.revocat.daidai.utils.JWTUtil;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/coin")
public class CoinController {
    @Resource
    CoinService service;

    @PostMapping("/info")
    public JSONObject getCoinInfo(@RequestBody JSONObject json) {
        String openID = JWTUtil.decodeToken(json.getString("token"));
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getCoinInfo(openID);
    }

    @PostMapping("/records")
    public JSONObject getCoinRecords(@RequestBody JSONObject json) {
        String openID = JWTUtil.decodeToken(json.getString("token"));
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        Integer maxCount = json.getInteger("maxcount");
        Integer page = json.getInteger("page");
        return service.getCoinRecords(openID, maxCount, page);
    }
}
