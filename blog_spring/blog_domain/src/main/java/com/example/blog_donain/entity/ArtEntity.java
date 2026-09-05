package com.example.blog_donain.entity;


import lombok.Data;

/**
 *文章表
 */
@Data
public class ArtEntity {

    private Long id; //文章id
    private String artname; //文章名称
    private long userid; //作者id
    private long catid; //分类id
    private String frnmianurl; //封面图片url
    private String sum; //文章摘要
    private String cont; //文章内容
    private String cjtime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
