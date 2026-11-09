package com.example.blog_domain.service.catservice;


import com.example.blog_common.result.Result;

/**
 * 删除分类服务接口
 * @author palpitate
 * @date 2026/11/09
 */
public interface DeleteCatService {

    Result deleteCat(long id);
}
