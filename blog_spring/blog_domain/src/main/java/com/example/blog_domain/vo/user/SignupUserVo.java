package com.example.blog_domain.vo.user;


import lombok.Data;

/**
 * 用户注册vo
 * @author palpitate
 * @date 2023/09/04
 */
@Data
public class SignupUserVo {
    private Long id; // 用户id
    private String username; // 用户名
    private String password; // 密码
    private String objecturl; // 头像
}
