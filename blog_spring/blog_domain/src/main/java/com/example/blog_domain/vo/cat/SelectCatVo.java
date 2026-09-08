package com.example.blog_domain.vo.cat;


import lombok.Data;

/**
 * 查询分类数据传输对象
 * @author palpitate
 * @date 2023/09/05
 */
@Data
public class SelectCatVo {

    private Integer id; // 分类ID
    private String catname; // 分类名称
}
