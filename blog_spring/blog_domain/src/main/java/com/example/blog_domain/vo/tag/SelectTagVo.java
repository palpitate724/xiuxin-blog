package com.example.blog_domain.vo.tag;


import lombok.Data;

/**
 * 查询标签列表的VO
 * @author palpitate
 * @date 2023/09/05
 */
@Data
public class SelectTagVo {

    private Long id; // 标签ID
    private String tagname; // 标签名
}
