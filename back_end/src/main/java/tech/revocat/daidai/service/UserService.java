package tech.revocat.daidai.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import tech.revocat.daidai.entity.User;

public interface UserService {
//    UserUpdateReq findUserByID(int id);


    User findUserByOpenID(String openID);

    // 新增用户
    void saveUser(String openID, String username, String avatar);

    JSONObject Login(String code);

//    @Deprecated
//    JSONObject updateInfo(String openID, String username, String avatar);

    JSONObject getInfo(String openID);

    JSONObject getLastInfo(String openID);

    JSONObject uploadAvatar(MultipartFile avatar,String openID);
}
