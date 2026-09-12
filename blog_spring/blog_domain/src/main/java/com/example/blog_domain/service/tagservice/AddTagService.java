package com.example.blog_domain.service.tagservice;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.tag.AddTagDto;

/**
 * 添加标签服务
 * @author palpitate
 * @date 2026/09/09
 */
public interface AddTagService {

    Result addTag(AddTagDto addTagDto);
}
