package com.example.blog_common.bctypt;

import com.example.blog_common.utils.bcrypt.BcryptUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BcryptUtils 工具类单元测试
 * 测试密码加密和验证功能
 */
public class TestBctyptUtils {

    private static final String PASSWORD = "admin";

    /**
     * 测试密码加密功能
     * 验证加密后的密码不为空且与原密码不同
     */
    @Test
    public void testJiami() {
        System.out.println("=== 测试密码加密功能 ===");
        System.out.println("原始密码: " + PASSWORD);
        
        String encodedPassword = BcryptUtils.jiami(PASSWORD);
        System.out.println("加密后密文: " + encodedPassword);
        System.out.println("密文长度: " + encodedPassword.length());
        
        assertNotNull(encodedPassword, "加密后的密码不应为null");
        assertNotEquals(PASSWORD, encodedPassword, "加密后的密码应与原密码不同");
        assertTrue(encodedPassword.length() > 0, "加密后的密码不应为空");
        assertTrue(encodedPassword.startsWith("$2a$"), "BCrypt加密结果应以$2a$开头");
        System.out.println("✓ 密码加密测试通过");
    }

    /**
     * 测试密码验证功能
     * 验证正确密码可以验证通过，错误密码验证失败
     */
    @Test
    public void testJiemi() {
        System.out.println("=== 测试密码验证功能 ===");
        String encodedPassword = BcryptUtils.jiami(PASSWORD);
        System.out.println("原始密码: " + PASSWORD);
        System.out.println("加密密文: " + encodedPassword);
        
        boolean isValid = BcryptUtils.jiemi(PASSWORD, encodedPassword);
        System.out.println("正确密码验证结果: " + isValid);
        assertTrue(isValid, "正确密码应该验证通过");
        
        boolean isInvalid = BcryptUtils.jiemi("wrongpassword", encodedPassword);
        System.out.println("错误密码验证结果: " + isInvalid);
        assertFalse(isInvalid, "错误密码应该验证失败");
        System.out.println("✓ 密码验证测试通过");
    }

    /**
     * 测试相同密码多次加密产生不同密文
     * 验证BCrypt的盐值随机性
     */
    @Test
    public void testEncryptionUniqueness() {
        System.out.println("=== 测试加密唯一性 ===");
        String encoded1 = BcryptUtils.jiami(PASSWORD);
        String encoded2 = BcryptUtils.jiami(PASSWORD);
        
        System.out.println("第一次加密: " + encoded1);
        System.out.println("第二次加密: " + encoded2);
        
        assertNotEquals(encoded1, encoded2, "相同密码多次加密应产生不同密文");
        
        // 验证两个密文都能验证原密码
        assertTrue(BcryptUtils.jiemi(PASSWORD, encoded1), "第一个密文应能验证原密码");
        assertTrue(BcryptUtils.jiemi(PASSWORD, encoded2), "第二个密文应能验证原密码");
        System.out.println("✓ 加密唯一性测试通过");
    }
}
