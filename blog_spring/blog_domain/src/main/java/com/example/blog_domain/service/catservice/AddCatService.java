package com.example.blog_domain.service.catservice;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.cat.AddCatDto;

/**
 * 添加分类服务接口
 * @author palpitate
 * @date 2026/09/09
 */
public interface AddCatService {

    /**
     * 添加分类
     * @param addCatDto
     * @return
     */
    Result addCat(AddCatDto addCatDto);
}
