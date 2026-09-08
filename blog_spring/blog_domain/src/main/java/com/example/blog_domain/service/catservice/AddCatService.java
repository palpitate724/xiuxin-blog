package com.example.blog_domain.service.catservice;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.cat.AddCatDto;

/**
 * 添加分类服务接口
 * @author palpitate
 * @date 2023/09/05
 */
public interface AddCatService {

    Result addCat(AddCatDto acd);
}
