package com.example.blog_domain.service.artservice;


import com.example.blog_common.result.Result;

/**
 * 删除文章服务接口
 * @author palpitate
 * @date 2026/09/10
 */
public interface DeleteArtService {

    /**
     * 删除文章
     */
    Result deleteArt(Long artid);
}
