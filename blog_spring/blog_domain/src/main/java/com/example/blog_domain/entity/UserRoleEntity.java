package com.example.blog_domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *用户角色表
 * @author palpitate
 * @date 2023/09/04
 */
@Data
@TableName("user_role")
public class UserRoleEntity {

    private Long id; //映射id
    private Long userid; //用户id
    private Long roleid; //角色id

}
