package com.example.blog_common.result;


import lombok.Data;

/**
 * 统一返回结果类
 * @author palpitate
 * @date 2026/09/08
 */
@Data
public class Result {
    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 私有构造函数，只能通过静态方法获取实例
     */
    private Result() {
    }

    /**
     * 获取实例
     * @return Result实例
     */
    public static Result getInstance() {
        return new Result();
    }
}