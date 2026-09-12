package com.example.blog_web.config;

import java.lang.annotation.*;

/**
 * 日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /** 操作模块（如：用户管理、订单处理） */
    String module() default "";

    /** 操作描述（如：新增用户、删除订单） */
    String description() default "";

}
