package com.example.blog_domain.service.artservice;

import com.example.blog_domain.dto.art.AddArtDto;

import com.example.blog_common.result.Result;

/**
 * ArtMinio服务接口
 * @author palpitate
 * @date 2023/09/05
 */
public interface AddArtService {

    Result addArt(AddArtDto aad);
}
