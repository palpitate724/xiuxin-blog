package com.example.blog_domain.vo.arttag;


import lombok.Data;

/**
 * 选择文章标签的VO
 * author palpitate
 * @date 2026/09/09
 */
@Data
public class SelectArtTagVo {

    private Long artid; // 文章ID
    private Long tagid; // 标签ID
    private String tagname; // 标签名称
}
