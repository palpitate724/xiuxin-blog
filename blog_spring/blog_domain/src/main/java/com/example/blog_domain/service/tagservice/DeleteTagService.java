package com.example.blog_domain.service.tagservice;


import com.example.blog_common.result.Result;

/**
 * 删除标签服务
 * @author palpitate
 * @date 2026/11/08
 */
public interface DeleteTagService {


    /**
     * 根据id 删除tag
     * @param id
     */
    Result deleteTag(Long id);

}
