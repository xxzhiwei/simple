package com.aojiaodage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    // 有效期
    public static final Long TOKEN_TTL = 60 * 60 * 1000L; // token过期时间（单位为毫秒）
    public static final Long REFRESH_TOKEN_TTL = TOKEN_TTL * 24 * 7; // refresh_token过期时间
    private static final String ISSUER = "tulingshop";

    // 秘钥明文【需要偶数个字符
    public static final String JWT_KEY = "at19o0o2l4";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return String
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    public static String createJWT(String subject) {
        return createJWT(subject, null);
    }

    public static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String id, String issuer) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.TOKEN_TTL;
        }
        if (!StringUtils.hasText(issuer)) {
            issuer = ISSUER;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(id) // 唯一的ID
                .setSubject(subject) // 主题（可以是JSON数据
                .setIssuer(issuer) // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String id) {
        return getJwtBuilder(subject, ttlMillis, id, null);
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return SecretKey
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     * @param token token
     * @return Claims
     */
    public static Claims parseJWT(String token) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
