package com.example.blog_common.jwt;

import com.example.blog_common.config.JwtProperties;
import com.example.blog_common.utils.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtils 工具类单元测试
 * 测试 JWT token 的生成、密钥获取和签名验证功能
 */
@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 测试前置准备
     * 初始化 JwtProperties 和 JwtUtils 实例
     */
    @BeforeEach

    public void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey("test-secret-key-for-jwt-token-generation-12345678");
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Test
    public void teGetjwtpro() {
        System.out.println("=== 测试 JwtUtils 类 ===");
        System.out.println("JwtProperties: " + jwtProperties.getSecretKey());
        System.out.println("✓ JwtUtils 测试通过");
    }

    /**
     * 测试 SecretKey 方法
     * 验证生成的随机密钥不为空且算法为 HmacSHA256
     */
    @Test
    public void testSecretKey() {
        System.out.println("=== 测试 SecretKey 方法 ===");
        SecretKey secretKey = jwtUtils.SecretKey();
        System.out.println("生成的密钥算法: " + secretKey.getAlgorithm());
        System.out.println("密钥编码长度: " + secretKey.getEncoded().length + " bytes");
        assertNotNull(secretKey);
        assertEquals("HmacSHA256", secretKey.getAlgorithm());
        System.out.println("✓ SecretKey 测试通过");
    }

    /**
     * 测试 getSecretKey 方法
     * 验证基于配置生成的密钥不为空且算法为 HmacSHA256
     */
    @Test
    public void testGetSecretKey() {
        System.out.println("=== 测试 getSecretKey 方法 ===");
        SecretKey secretKey = jwtUtils.getSecretKey();
        System.out.println("生成的密钥算法: " + secretKey.getAlgorithm());
        System.out.println("密钥编码长度: " + secretKey.getEncoded().length + " bytes");
        assertNotNull(secretKey);
        assertEquals("HmacSHA256", secretKey.getAlgorithm());
        System.out.println("✓ getSecretKey 测试通过");
    }

    /**
     * 测试 getToken 方法
     * 验证生成的 token 不为空且格式正确（包含点分隔符）
     */
    @Test
    public void testGetToken() {
        System.out.println("=== 测试 getToken 方法 ===");
        Long id = 1L;
        String username = "testuser";
        System.out.println("用户ID: " + id);
        System.out.println("用户名: " + username);
        String token = jwtUtils.getToken(id, username);
        System.out.println("生成的Token长度: " + token.length());
        System.out.println("Token包含分隔符: " + token.contains("."));
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
        System.out.println("✓ getToken 测试通过");
    }

    /**
     * 测试 getToken 方法的 claims 内容
     * 验证 token 中包含正确的 id、username、签发时间和过期时间
     */
    @Test
    public void testGetTokenClaims() {
        System.out.println("=== 测试 getToken Claims 内容 ===");
        Long id = 123L;
        String username = "testuser";
        System.out.println("输入用户ID: " + id);
        System.out.println("输入用户名: " + username);
        String token = jwtUtils.getToken(id, username);

        SecretKey secretKey = jwtUtils.getSecretKey();
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println("Token中的ID: " + claims.get("id"));
        System.out.println("Token中的用户名: " + claims.get("username"));
        System.out.println("签发时间: " + claims.getIssuedAt());
        System.out.println("过期时间: " + claims.getExpiration());
        assertEquals(id, claims.get("id"));
        assertEquals(username, claims.get("username"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        System.out.println("✓ getTokenClaims 测试通过");
    }

    /**
     * 测试 token 签名验证
     * 验证使用正确密钥可以成功解析 token
     */
    @Test
    public void testTokenSignature() {
        System.out.println("=== 测试 Token 签名验证 ===");
        String token = jwtUtils.getToken(1L, "testuser");
        System.out.println("生成的Token: " + token.substring(0, Math.min(50, token.length())) + "...");
        SecretKey secretKey = jwtUtils.getSecretKey();
        System.out.println("使用密钥验证签名...");

        assertDoesNotThrow(() -> {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        });
        System.out.println("✓ Token 签名验证通过");
    }
}
