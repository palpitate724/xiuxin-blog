package com.example.blog_domain.vo.art;

import com.example.blog_domain.vo.tag.TagVo;
import lombok.Data;

import java.util.List;


/**
 * 完整文章查询vo
 * @author palpitate
 * @date 2026/09/19
 */
@Data
public class SelectArtByIdVo {

    private Long id; // 文章ID
    private String name; // 文章标题
    private Long userid; // 用户ID
    private Long catid; // 分类ID
    private String fenmianurl; // 封面URL
    private String sum; // 文章摘要
    private String cont; //文章内容

    private List<TagVo> tagvolist; // 标签列表
}
