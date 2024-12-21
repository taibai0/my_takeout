package com.zwx.utils;

import io.jsonwebtoken.*;


import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法，私钥使用固定密钥
     *
     * @param secretKey
     * @param ttlMillis
     * @param claims
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        //指定签名时使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date expDate = new Date(expMillis);

        //设置jwt的body
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(expDate);

        return builder.compact();
    }

    public static Claims parseJWT(String secretKey, String token) {
      
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt,方法是jws不是jwt！！！！
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
