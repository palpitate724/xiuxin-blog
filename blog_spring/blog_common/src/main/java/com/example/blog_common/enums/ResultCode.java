package com.example.blog_common.enums;

import lombok.Getter;

/**
 * 结果状态码枚举
 * 定义接口返回的各种状态码
 */
@Getter
public enum ResultCode {
    
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 失败
     */
    FAIL(400, "操作失败"),
    
    /**
     * 未认证
     */
    UNAUTHORIZED(401, "未认证"),
    
    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限"),
    
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    
    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    /**
     * 参数错误
     */
    PARAM_ERROR(1001, "参数错误"),
    
    /**
     * 参数为空
     */
    PARAM_EMPTY(1002, "参数为空"),
    
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(2001, "用户不存在"),
    
    /**
     * 用户已存在
     */
    USER_ALREADY_EXIST(2002, "用户已存在"),
    
    /**
     * 密码错误
     */
    PASSWORD_ERROR(2003, "密码错误"),
    
    /**
     * Token无效
     */
    TOKEN_INVALID(2004, "Token无效"),
    
    /**
     * Token过期
     */
    TOKEN_EXPIRED(2005, "Token过期"),
    
    /**
     * 文章不存在
     */
    ARTICLE_NOT_EXIST(3001, "文章不存在"),
    
    /**
     * 文章已删除
     */
    ARTICLE_DELETED(3002, "文章已删除"),

    /**
     * 文章添加失败
     */
    ARTICLE_NOT_INSERT(3003,"文章插入失败"),

    /**
     * 评论不存在
     */
    COMMENT_NOT_EXIST(4001, "评论不存在"),
    
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    
    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_SUPPORTED(5002, "文件类型不支持"),

    /**
     * 资源请求失败
     */
    RESOURCE_REQUEST_FAILED(6001, "资源请求失败"),

    /**
     * 分类已存在
     */
    CATEGORY_ALREADY_EXIST(7001, "分类已存在"),

    /**
     * 分类不存在
     */
    CATEGORY_NOT_EXIST(7002, "分类不存在"),

    /**
     * 标签已存在
     */
    TAG_ALREADY_EXIST(8001, "标签已存在"),
    /**
     * 标签不存在
     */
    TAG_NOT_EXIST(8002, "标签不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 对应的枚举值
     */
    public static ResultCode getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }
}
