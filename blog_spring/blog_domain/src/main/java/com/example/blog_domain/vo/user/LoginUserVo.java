package com.example.blog_domain.vo.user;


import lombok.Data;

/**
 * 登录用户信息Vo
 * @author palpitate
 * @date 2026/09/09
 */
@Data
public class LoginUserVo {
    private Long id;
    private String username; // 用户名
    private String touxiangurl; // 头像地址
    private String token; // token
}
