package com.example.blog_domain.vo.art;

import com.example.blog_domain.vo.cat.SelectCatVo;
import com.example.blog_domain.vo.tag.TagVo;
import com.example.blog_domain.vo.user.ArtUserVo;
import lombok.Data;

import java.util.List;


/**
 * 完整文章查询vo
 * @author palpitate
 * @date 2026/09/19
 */
@Data
public class SelectArtByIdVo {

    private String id; // 文章ID
    private String name; // 文章标题
    private String fenmianurl; // 封面URL
    private String sum; // 文章摘要
    private String cont; //文章内容
    private String cjiantime; //创建时间
    private String uptime; //更新时间
    private int status; //状态 0:正常 1:草稿

    /**
     * 用户VO
     * id
     * name
     */
    private ArtUserVo uservo; // 用户VO
    /**
     * 分类VO
     * id
     * name
     */
    private SelectCatVo catvo; // 分类VO
    /**
     * 标签列表
     * id
     * name
     */
    private List<TagVo> tagvolist; // 标签列表
}
