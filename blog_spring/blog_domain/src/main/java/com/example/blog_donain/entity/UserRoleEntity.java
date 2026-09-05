package com.example.blog_donain.entity;


import lombok.Data;

/**
 *用户角色表
 */
@Data
public class UserRoleEntity {

    private Long id; //映射id
    private Long userid; //用户id
    private Long roleid; //角色id

}
