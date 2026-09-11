package com.example.blog_domain.service.artservice;

import com.example.blog_domain.dto.art.AddArtDto;

import com.example.blog_common.result.Result;

/**
 * ArtMinio服务接口
 * @author palpitate
 * @date 2026/09/09
 */
public interface AddArtService {


    /**
     * 添加文章
     * @param addArtDto
     * @return
     */
    Result addArt(AddArtDto addArtDto);
}
