package com.example.blog_domain.service.catservice;


import com.example.blog_common.result.Result;

/**
 * 查询分类列表服务
 * @author palpitate
 * @date 2026/09/09
 */
public interface SelectCatListService {


    /**
     * 查询分类列表
     * @return
     */
    Result selectListCat();
}
