package com.example.blog_domain.dto.user;


import lombok.Data;

/**
 * 用户登录Dto
 * @author palpitate
 * @date 2026/09/08
 */
@Data
public class LoginUserDto {
    private String username; // 用户名
    private String password; // 密码
}
