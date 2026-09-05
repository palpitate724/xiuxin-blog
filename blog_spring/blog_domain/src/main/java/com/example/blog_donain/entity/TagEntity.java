package com.example.blog_donain.entity;


import lombok.Data;

/**
 *标签表
 */
@Data
public class TagEntity {

    private Long id; //标签id
    private String tagname; //标签名称
    private String cjiantime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
