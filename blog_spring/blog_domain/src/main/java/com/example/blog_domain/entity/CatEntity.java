package com.example.blog_domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 分类表
 * @author palpitate
 * @date 2026/09/08
 */
@Data
@TableName("cat")
public class CatEntity {

    private Long id; //分类ID
    private String catname; //分类名称
    private String cjiantime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
