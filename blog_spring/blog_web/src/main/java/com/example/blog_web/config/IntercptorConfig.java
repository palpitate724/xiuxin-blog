package com.example.blog_web.config;


import com.example.blog_web.intercrptor.TokenIntercrptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * @author palpitate
 * @date 2026/09/10
 */
@Configuration
public class IntercptorConfig implements WebMvcConfigurer {

    private final TokenIntercrptor tokenIntercrptor;
    public IntercptorConfig(TokenIntercrptor tokenIntercrptor) {
        this.tokenIntercrptor = tokenIntercrptor;
    }

    /**
     * 添加token拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenIntercrptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/user/login", "/api/v1/user/signup");
    }


}
