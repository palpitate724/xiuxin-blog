package com.example.blog_domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *文章标签表
 * @author palpitate
 * @date 2026/09/08
 */
@Data
@TableName("art_tag")
public class ArtTagEntity {

    private Long id; //映射id
    private Long artid; //文章id
    private Long tagid; //标签id

}
