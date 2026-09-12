package com.example.blog_domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Retention;

/**
 * BlogDomain 模块集成测试
 * 验证 Spring 上下文加载
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BlogDomainApplicationTests {

    /**
     * 测试 Spring 上下文加载
     * 验证应用配置正确，所有 Bean 可以正常初始化
     */
    @Test
    void contextLoads() {
        System.out.println("=== 测试 Spring 上下文加载 ===");
        System.out.println("✓ Spring 上下文加载成功");
    }

}
