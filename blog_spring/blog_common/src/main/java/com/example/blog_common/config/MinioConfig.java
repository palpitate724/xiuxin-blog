package com.example.blog_common.config;


import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类，用于配置Minio服务的连接信息
 * @author palpitate
 * @date 2026/09/09
 */
@Configuration
public class MinioConfig {

    private final MinioProperties minioProperties;
    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    /**
     * 配置minio服务端
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }



}
