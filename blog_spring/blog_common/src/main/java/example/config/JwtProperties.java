package example.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * 密钥映射
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.secretary")
public class JwtProperties {
    private String secretary;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretary.getBytes(StandardCharsets.UTF_8));
    }
}
