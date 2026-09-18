package com.example.blog_domain.service.artservice;


import com.example.blog_common.result.Result;

/**
 * SelectArtListService 接口
 * @author palpitate
 * @date 2026/09/09
 */
public interface SelectArtPageService {

    /**
     * 分页查询文章列表
     * @return
     */
    Result selectArtPage(Integer page, Integer size);
}
