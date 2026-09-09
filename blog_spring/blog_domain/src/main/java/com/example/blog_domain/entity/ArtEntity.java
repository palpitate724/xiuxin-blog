package com.example.blog_domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *文章表
 * @author palpitate
 * @date 2026/09/08
 */
@Data
@TableName("art")
public class ArtEntity {

    private Long id; //文章id
    private String artname; //文章名称
    private long userid; //作者id
    private long catid; //分类id
    private String fenmianurl; //封面图片url
    private String sum; //文章摘要
    private String cont; //文章内容
    private String cjiantime; //创建时间
    private int deleted; //删除标志 0:未删除 1:已删除

}
