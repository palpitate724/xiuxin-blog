package com.example.blog_common.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 密钥映射
 * @author palpitate
 * @date 2026/09/08
 */
@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtProperties {
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}
