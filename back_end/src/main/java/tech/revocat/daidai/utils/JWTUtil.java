package tech.revocat.daidai.utils;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Repository
public class JWTUtil {

    private static final String SECRET = "1145141919810";
    private static final String ISSUER = "NullPointerException";
    private static final Long EXPIRE_DATE = 24 * 1000 * 60 * 60L;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    // 解密token
    public static String decodeToken(String token) {
        try {
            Claim result = JWT.require(Algorithm.HMAC256(SECRET)).build().
                    verify(token).getClaim("openid");

            if (result.isNull()) {
                return "";
            }
            return result.asString();
        } catch (JWTVerificationException jwtVerificationException) {
            return "";
        }
    }

    // 验证jwt过期
    public static JSONObject isTokenExpired(String token) {
        if ("".equals(token)) {
            JSONObject object = new JSONObject();
            object.put("success", 0);
            object.put("msg", "Token已过期或无效");
            return object;
        }
        return null;
    }

    // 生成token
    public String generateToken(String openID) {
        // 尝试从redis取
        String redisToken = stringRedisTemplate.opsForValue().get("User:" + openID);
        if (redisToken != null) {
            return redisToken;
        }
        Date now = new Date();
        // 过期时间
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.DATE, 7);
        // header
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + EXPIRE_DATE))
                .withClaim("openid", openID)
                .sign(algorithm);
        // 写入redis
        stringRedisTemplate.opsForValue().set("User:" + openID, token, 1, TimeUnit.DAYS);
        return token;
    }
}
