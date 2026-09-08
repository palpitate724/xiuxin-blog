package com.example.blog_common.utils.bcrypt;


import org.mindrot.jbcrypt.BCrypt;

/**
 * BCrypt工具类
 * @author palpitate
 * @date 2023/09/04
 */
public class BcryptUtils {

    /**
     * 加密密码
     */
    public static String jiami(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 验证密码
     */
    public static boolean jiemi(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }


}
