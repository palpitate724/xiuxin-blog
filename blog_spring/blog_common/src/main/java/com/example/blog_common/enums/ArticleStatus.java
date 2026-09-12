package com.example.blog_common.enums;

import lombok.Getter;

/**
 * 文章状态枚举
 * 定义文章的各种状态
 */
@Getter
public enum ArticleStatus {
    
    /**
     * 草稿状态
     */
    DRAFT(0, "草稿"),
    
    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),
    
    /**
     * 已下架
     */
    OFFLINE(2, "已下架"),
    
    /**
     * 已删除
     */
    DELETED(3, "已删除");

    private final Integer code;
    private final String desc;

    ArticleStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 对应的枚举值
     */
    public static ArticleStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ArticleStatus status : ArticleStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
