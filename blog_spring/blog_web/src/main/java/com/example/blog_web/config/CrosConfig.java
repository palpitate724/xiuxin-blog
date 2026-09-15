package com.example.blog_web.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * @author palpitate
 * @date 2026/09/13
 */
@Configuration
public class CrosConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 设置允许跨域的路径
                .allowedOriginPatterns("*")  // 设置允许跨域请求的源
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 设置允许的请求方法
                .allowedHeaders("*")  // 设置允许的请求头
                .maxAge(3600)
                .allowCredentials(true);  // 设置是否允许发送Cookie
    }
}
