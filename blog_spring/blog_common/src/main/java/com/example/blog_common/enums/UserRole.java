package com.example.blog_common.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 * 定义用户的各种角色
 */
@Getter
public enum UserRole {
    
    /**
     * 普通用户
     */
    USER(0L, "普通用户"),
    
    /**
     * 作者
     */
    AUTHOR(1L, "作者"),
    
    /**
     * 管理员
     */
    ADMIN(2L, "管理员"),
    
    /**
     * 超级管理员
     */
    SUPER_ADMIN(3L, "超级管理员");

    private final Long code;
    private final String desc;

    UserRole(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据角色码获取枚举
     * @param code 角色码
     * @return 对应的枚举值
     */
    public static UserRole getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserRole role : UserRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }
}
