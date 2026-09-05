package com.example.blog_donain.entity;


import lombok.Data;

/**
 *文章标签表
 */
@Data
public class ArtTagEntity {

    private Long id; //映射id
    private Long artid; //文章id
    private Long tagid; //标签id

}
