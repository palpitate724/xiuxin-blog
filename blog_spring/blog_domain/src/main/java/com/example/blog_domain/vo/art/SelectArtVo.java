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
public class SelectArtVo {

    private String id; // 文章ID
    private String name; // 文章标题
    private String userid; // 用户ID
    private String catid; // 分类ID
    private String fenmianurl; // 封面URL
    private String sum; // 文章摘要
    private String cjiantime; //创建时间
    private String uptime; //更新时间
    private int status; //状态 0:正常 1:草稿

    private List<TagVo> tagvolist; // 标签列表
}
