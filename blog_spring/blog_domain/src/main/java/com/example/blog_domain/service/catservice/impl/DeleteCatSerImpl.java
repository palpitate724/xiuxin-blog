package com.example.blog_domain.service.catservice.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.entity.ArtEntity;
import com.example.blog_domain.mapper.ArtMapper;
import com.example.blog_domain.mapper.CatMapper;
import com.example.blog_domain.service.catservice.DeleteCatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Wrapper;

/**
 * 删除分类服务实现类
 * @author palpitate
 * @date 2026/11/09
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class DeleteCatSerImpl implements DeleteCatService {

    private final ArtMapper artMapper;
    private final CatMapper catMapper;
    public DeleteCatSerImpl(ArtMapper artMapper, CatMapper catMapper) {
        this.artMapper = artMapper;
        this.catMapper = catMapper;
    }

    @Override
    public Result deleteCat(long id) {
        Result result = Result.getInstance();

        try{
            // 删除分类
            // 检索是否还有关联文章
            if (artMapper.selectCount(
                    Wrappers.<ArtEntity>lambdaQuery().eq(ArtEntity::getCatid, id)
            ) > 0) {
                result.setCode(ResultCode.CATEGORY_HAS_ARTICLE.getCode());
                result.setMessage(ResultCode.CATEGORY_HAS_ARTICLE.getMessage());
                result.setData(null);
            }
            else{
                // 无则删除
                catMapper.deleteById(id);
                result.setCode(ResultCode.SUCCESS.getCode());
                result.setMessage(ResultCode.SUCCESS.getMessage());
                result.setData(null);
            }

        }catch (Exception e){
            result.setCode(ResultCode.FAIL.getCode());
            result.setMessage(ResultCode.FAIL.getMessage());
            result.setData(null);
        }

        return result;
    }

}
