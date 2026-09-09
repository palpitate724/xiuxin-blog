package com.example.blog_domain.vo.art;


import com.example.blog_domain.vo.tag.TagVo;
import lombok.Data;

import java.util.List;

/**
 * 用于查询文章列表的VO
 * @author palpitate
 * @date 2026/09/08
 */
@Data
public class SelectArtListVo {

    private Long id; // 文章ID
    private String artname; // 文章标题
    private String cont; // 文章内容
    private Long userid; // 用户ID
    private Long catid; // 分类ID
    private String fenmianurl; // 封面URL
    private String sum; // 文章摘要

    private List<TagVo> tagvolist; // 标签列表
}
