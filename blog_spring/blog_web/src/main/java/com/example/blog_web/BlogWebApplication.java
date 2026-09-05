package com.example.blog_web;

import com.example.config.JwtProperties;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BlogWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogWebApplication.class, args);
    }

}
