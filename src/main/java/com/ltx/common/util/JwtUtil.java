package com.ltx.common.util;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.common.constant.Constant;
import com.ltx.entity.po.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Jwt工具类
 *
 * @author tianxing
 */
@Component
public class JwtUtil {

    private static final String SECRET = "/r3cvNod5rgpBq69NuSX1eseTdx4xiJQYRTJcGKovlE=";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private final JacksonSerializer<Map<String, ?>> jsonSerializer;
    private final JacksonDeserializer<Map<String, ?>> jsonDeserializer;

    public JwtUtil(ObjectMapper objectMapper) {
        this.jsonSerializer = new JacksonSerializer<>(objectMapper);
        this.jsonDeserializer = new JacksonDeserializer<>(objectMapper);
    }

    /**
     * 生成密钥
     *
     * @return 密钥
     */
    public static String genSecret() {
        // 生成适合HMAC-SHA-256算法的密钥
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 创建JWS
     *
     * @param user     用户
     * @param timeout  有效时长
     * @param timeUnit 时间单位
     * @return JWS
     */
    public String createJws(User user, long timeout, TimeUnit timeUnit) {
        // 发行时间
        Date issueDate = new Date();
        // 过期时间
        Date expireDate = new Date(issueDate.getTime() + timeUnit.toMillis(timeout));
        // 自定义声明
        Map<String, Object> claimMap = new HashMap<>();
        // 用户信息
        claimMap.put(Constant.USER, user);
        return Jwts.builder()
                .json(jsonSerializer)
                .id(UUID.randomUUID().toString())
                .issuedAt(issueDate)
                .expiration(expireDate)
                .claims(claimMap)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWS
     *
     * @param jws JWS
     * @return JWS
     */
    public Jws<Claims> parseJws(String jws) {
        return Jwts.parser().json(jsonDeserializer).verifyWith(SECRET_KEY).build().parseSignedClaims(jws);
    }


    /**
     * 获取载荷
     *
     * @param jws JWS
     * @return 载荷
     */
    public Claims getPayLoad(String jws) {
        return parseJws(jws).getPayload();
    }
}
