package com.example.blog_domain.service.artservice;


import com.example.blog_common.result.Result;

/**
 * SelectArtService 接口
 * @author palpitate
 * @date 2026/09/18
 */
public interface SelectArtService {
    Result selectArtById(Long id);
}
