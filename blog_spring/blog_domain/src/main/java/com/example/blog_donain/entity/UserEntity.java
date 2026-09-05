package com.example.blog_donain.entity;

import lombok.Data;

/**
 *用户表
 */
@Data
public class UserEntity {

    private Long id; //用户ID
    private String username; //用户名
    private String userpassword; //密码
    private String touxiangurl; //头像地址
    private String email; //邮箱
    private String cjiantime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
