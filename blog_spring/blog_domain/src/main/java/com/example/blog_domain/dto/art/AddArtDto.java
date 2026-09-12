package com.example.blog_domain.dto.art;


import lombok.Data;

import java.util.List;

/**
 * AddArtDto 用于添加文章的DTO
 * @author palpitate
 * @date 2026/09/09
 */
@Data
public class AddArtDto {


    private String artname; // 文章名称
    private Long userid; // 用户ID
    private Long catid; // 分类ID
    private String fenmianurl; // 封面URL
    private String sum; // 文章摘要
    private String cont; // 文章内容

    private List<Long> tagids; // 标签列表
}

