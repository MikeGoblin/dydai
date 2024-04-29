package tech.revocat.daidai.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.revocat.daidai.service.UserService;
import tech.revocat.daidai.utils.JWTUtil;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService service;

    @Resource
    JWTUtil jwtUtil;

    // 登录
    @PostMapping("/login")
    public JSONObject Login(@RequestBody JSONObject json) {
        String code = json.getString("code");
        return service.Login(code);
    }

    // 更新用户信息
//    @PostMapping("/update-info")
//    @Deprecated
//    public JSONObject updateInfo(@RequestBody User user) {
//        String openID = JWTUtil.decodeToken(user.getToken());
//        JSONObject info = JWTUtil.isTokenExpired(openID);
//        if (info != null) {
//            return info;
//        }
//        return service.updateInfo(openID, user.getUsername(), user.getAvatar());
//    }

    //查询用户信息
    @PostMapping("/get-info")
    public JSONObject getInfo(@RequestBody JSONObject json) {
        String openID = JWTUtil.decodeToken(json.getString("token"));
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getInfo(openID);
    }

    // 查询用户上一次收货信息
    @PostMapping("/last-info")
    public JSONObject getLastInfo(@RequestBody JSONObject json) {
        String openID = JWTUtil.decodeToken(json.getString("token"));
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.getLastInfo(openID);
    }

    @PostMapping("/upload")
    public JSONObject uploadFile(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "avatar") MultipartFile avatar) {
        String openID = JWTUtil.decodeToken(token);
        JSONObject info = JWTUtil.isTokenExpired(openID);
        if (info != null) {
            return info;
        }
        return service.uploadAvatar(avatar,openID);
    }
}
