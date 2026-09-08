package com.example.blog_common.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Minio 配置对象，用于存储 Minio 服务的连接信息
 * @author palpitate
 * @date 2023/09/04
 */
@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties {
    private String endpoint; // Minio 服务的地址
    private String bucketName; // 存储桶名称
    private String accessKey; // 访问密钥
    private String secretKey; // 秘密密钥
}
