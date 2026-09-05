package example.utils.jwt;

import com.example.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Jwt工具类
 */
@Configuration
public class JwtUtils {
//    @Autowired
    public JwtProperties jwtProperties = new JwtProperties();

   // 获取密钥
    public SecretKey getSecretKey(){
        return Jwts.SIG.HS256.key().build();
    }

    // 获取token
    public String getToken(Long id,String username){
        return Jwts.builder()
                .signWith(jwtProperties.getSecretKey())
                .claim("id",id)
                .claim("username",username)
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600*1000))
                .compact();
    }

}
