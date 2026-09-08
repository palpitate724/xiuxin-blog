package com.example.blog_domain.dto.user;


import lombok.Data;

/**
 * 用户注册Dto
 * @author palpitate
 * @date 2023/09/04
 */
@Data
public class SignupUserDto {

    private String username; // 用户名
    private String password; // 密码
    private String email; // 邮箱
    private String touxiangurl; // 头像
}
