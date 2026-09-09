package com.example.blog_common.utils.jwt;

import com.example.blog_common.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Jwt工具类
 * @author palpitate
 * @date 2026/09/08
 */
@Component
public class JwtUtils {

    private final JwtProperties jwtProperties;
    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


   // 获取密钥
    public SecretKey SecretKey(){
        return Jwts.SIG.HS256.key().build();
    }

    //string-secretkey
    public SecretKey getSecretKey(){
        String jwtSecret = jwtProperties.getSecretKey();
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 获取token
    public String getToken(Long id,String username){
        return Jwts.builder()
                .signWith(getSecretKey())
                .claim("id",id)
                .claim("username",username)
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600*1000))
                .compact();
    }

    // 校验token
    public boolean isTokenValid(String token){
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
