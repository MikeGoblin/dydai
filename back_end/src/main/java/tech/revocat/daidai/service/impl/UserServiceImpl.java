package tech.revocat.daidai.service.impl;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.revocat.daidai.entity.User;
import tech.revocat.daidai.mapper.UserMapper;
import tech.revocat.daidai.service.UserService;
import tech.revocat.daidai.utils.JWTUtil;
import tech.revocat.daidai.utils.LoginUtil;

import javax.annotation.Resource;
import java.io.File;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper mapper;

    @Resource
    JWTUtil jwtUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Value("${Wechat.appID}")
    private String appID;
    @Value("${Wechat.appSecret}")
    private String appSecret;


    @Override
    public User findUserByOpenID(String openID) {
        return mapper.findByOpenID(openID);
    }


    @Override
    public void saveUser(String openID, String username, String avatar) {
        mapper.saveUser(openID);
    }

    @Override
    public JSONObject Login(String code) {
        JSONObject object = new JSONObject();
        if ("".equals(code)) {
            System.out.println("code空");
            object.put("success", 0);
            object.put("msg", "code为空！");
            return object;
        } else {
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                    + appID + "&secret="
                    + appSecret + "&js_code="
                    + code + "&grant_type=authorization_code";
            String str = LoginUtil.httpRequest(url, "GET", null);
            // 获取openid
            JSONObject jsonObject = JSONObject.parseObject(str);
            String openID = (String) jsonObject.get("openid");
            String sessionKey = (String) jsonObject.get("session_key");
//            // Redis储存session_key
//            redisTemplate.opsForValue().set("openid:" + openID, sessionKey, 10, TimeUnit.HOURS);

            if (openID != null && !"".equals(openID)) {
                // 登录成功
//                User userVO = new User();
//                userVO.setUsername(loginUser.getUsername());
//                userVO.setAvatar(loginUser.getAvatar());
//                userVO.setOpenID(openID);
                // 生成jwt
                String token = jwtUtil.generateToken(openID);
                User user = mapper.findByOpenID(openID);
                if (user == null) {
                    // 首次登录
                    mapper.saveUser(openID);
                }
//                } else {
//                    // 非首次登录
//                    mapper.updateInfo(openID, loginUser.getUsername(), loginUser.getAvatar());
//                }
                object.put("success", 1);
                object.put("token", token);
            } else {
                // 登录失败
                object.put("success", 0);
                object.put("msg", "登录失败！");
            }
            return object;
        }
    }

//    @Deprecated
//    public JSONObject updateInfo(String openID, String username, String avatar) {
//        JSONObject object = new JSONObject();
//        if (openID == null || username == null || avatar == null) {
//            object.put("success", 0);
//            object.put("msg", "参数不足！");
//            return object;
//        }
//        // 非首次登录
//        mapper.updateInfo(openID, username, avatar);
//        System.out.println(openID);
//        System.out.println(username);
//        System.out.println(avatar);
//        object.put("success", 1);
//        return object;
//    }

    @Override
    public JSONObject getInfo(String openID) {
        JSONObject object = new JSONObject();
        if (openID == null) {
            object.put("success", 0);
            object.put("msg", "OpenID为空！");
        } else {
            User user = mapper.findByOpenID(openID);
            if (user == null) {
                object.put("success", 0);
                object.put("msg", "该用户不存在！");
            } else {
                object.put("success", 1);
                object.put("balance", user.getBalance());
                object.put("completed_order_count", user.getCompletedOrderCount());
                object.put("accepted_order_count", user.getAcceptedOrderCount());
                object.put("posted_order_count", user.getPostedOrderCount());
            }
        }
        return object;
    }

    @Override
    public JSONObject getLastInfo(String openID) {
        JSONObject object = new JSONObject();
        if (openID == null) {
            object.put("success", 0);
            object.put("msg", "OpenID为空！");
            return object;
        }
        // 尝试从redis取值
        String address = (String) stringRedisTemplate.opsForHash().get("Info:" + openID, "address");
        String phone = (String) stringRedisTemplate.opsForHash().get("Info:" + openID, "phone");
        if (address == null || phone == null) {
            object.put("success", 0);
            object.put("msg", "找不到上次收货信息！");
            return object;
        }
//            // 从mysql取值
//            User user = findUserByOpenID(openID);
//            stringRedisTemplate.opsForHash().put("Info:" + openID, "address", user.getAddress());
//            stringRedisTemplate.opsForHash().put("Info:" + openID, "phone", user.getPhone());
//            object.put("success", 1);
//            object.put("address", user.getAddress());
//            object.put("phone", user.getPhone());
//            return object;
//        }
        object.put("success", 1);
        object.put("address", address);
        object.put("phone", phone);
        return object;

    }

    @Override
    public JSONObject uploadAvatar(MultipartFile avatar, String openID) {
        JSONObject object = new JSONObject();
        if (openID == null || avatar == null) {
            object.put("success", 0);
            object.put("msg", "参数不足！");
            return object;
        }
//        String suffix = avatar.getOriginalFilename().substring(0,)
        File file = new File("./avatars" + openID);
        // TODO:文件上传功能
        return null;
    }
}
