package com.example.blog_donain.entity;


import lombok.Data;


/**
 * 角色表
 */
@Data
public class RoleEntity {

    private Long id; //角色id
    private String rolename; //角色名称
    private String cjiantime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
