package com.example.blog_domain.dto.arttag;


import lombok.Data;

import java.util.List;

/**
 * AddArtTagDto 类
 * @author palpitate
 * @date 2026/09/09
 */
@Data
public class AddArtTagDto {

    private Long artId; // 文章ID
    private List<Long> tagids; // 标签ID列表
}
