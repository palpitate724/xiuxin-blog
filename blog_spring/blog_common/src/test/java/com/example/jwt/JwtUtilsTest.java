package com.example.jwt;

import com.example.config.JwtProperties;
import com.example.utils.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class JwtUtilsTest {
    @Autowired
    public JwtProperties jwtProperties;
    public JwtUtils jwtUtils = new JwtUtils();

    @Test
    public void testJwtUtils() {
        System.out.println("密钥："+Base64.getEncoder().encodeToString(jwtUtils.getSecretKey().getEncoded()));
    }
    @Test
    public void getTokenTest(){
        System.out.println("token："+jwtUtils.getToken(1L,"test"));
    }
    @Test
    public void getSsecretaryTest(){
        System.out.println("密钥："+);
    }


}
