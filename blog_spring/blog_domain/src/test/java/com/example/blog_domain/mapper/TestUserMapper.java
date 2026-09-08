package com.example.blog_domain.mapper;


import com.example.blog_domain.entity.UserEntity;
import com.example.blog_common.utils.bcrypt.BcryptUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试用户Mapper
 */
@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    private Long testUserId;

    /**
     * 测试插入用户
     * 验证用户数据可以成功插入数据库
     */
    @Test
    void testInsertUser() {
        System.out.println("=== 测试插入用户 ===");
        
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setUserpassword(BcryptUtils.jiami("test"));
        user.setTouxiangurl("test");
        user.setEmail("test");
        user.setDeleted(0);
        
        System.out.println("用户名: " + user.getUsername());
        System.out.println("邮箱: " + user.getEmail());
        
        int result = userMapper.insert(user);
        System.out.println("插入结果: " + result);
        
        assertTrue(result > 0, "插入操作应影响至少一行");
        assertNotNull(user.getId(), "插入后应生成ID");
        
        testUserId = user.getId();
        System.out.println("生成的用户ID: " + testUserId);
        System.out.println("✓ 用户插入测试通过");
    }

    /**
     * 测试后清理
     * 删除测试数据，避免污染数据库
     */
    @AfterEach
    void cleanup() {
        if (testUserId != null) {
            System.out.println("=== 清理测试数据 ===");
            int deleteResult = userMapper.deleteById(testUserId);
            System.out.println("删除结果: " + deleteResult);
            assertTrue(deleteResult > 0, "删除操作应成功");
            System.out.println("✓ 测试数据清理完成");
        }
    }
}
