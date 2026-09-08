package com.example.blog_web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * BlogWeb 模块集成测试
 * 验证 Spring 上下文加载
 */
@SpringBootTest
class BlogWebApplicationTests {

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
