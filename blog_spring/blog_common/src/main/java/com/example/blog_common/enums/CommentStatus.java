package com.example.blog_common.enums;

import lombok.Getter;

/**
 * 评论状态枚举
 * 定义评论的各种状态
 */
@Getter
public enum CommentStatus {
    
    /**
     * 待审核
     */
    PENDING(0, "待审核"),
    
    /**
     * 已通过
     */
    APPROVED(1, "已通过"),
    
    /**
     * 已拒绝
     */
    REJECTED(2, "已拒绝"),
    
    /**
     * 已删除
     */
    DELETED(3, "已删除");

    private final Integer code;
    private final String desc;

    CommentStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 对应的枚举值
     */
    public static CommentStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CommentStatus status : CommentStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
