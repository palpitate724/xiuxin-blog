package com.example.blog_domain.service.tagservice.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.entity.ArtTagEntity;
import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.mapper.TagMapper;
import com.example.blog_domain.service.tagservice.DeleteTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 删除tag service实现类
 * @author palpitate
 * @date 2026/11/08
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class DeleteTagSerImpl implements DeleteTagService {


    private final ArtTagMapper artTagMapper;
    private final TagMapper tagMapper;
    public DeleteTagSerImpl(ArtTagMapper artTagMapper, TagMapper tagMapper) {
        this.artTagMapper = artTagMapper;
        this.tagMapper = tagMapper;
    }


    /**
     * 删除标签
     * @param id 标签id
     */
    @Override
    public Result deleteTag(Long id) {
        Result result = Result.getInstance();

        try{

            // 判断该标签是否还有关联文章
            if (artTagMapper.selectCount(
                    Wrappers.<ArtTagEntity>lambdaQuery().eq(ArtTagEntity::getTagid, id)
            ) > 0){
                result.setCode(ResultCode.TAG_HAS_ARTICLE.getCode());
                result.setMessage(ResultCode.TAG_HAS_ARTICLE.getMessage());
                result.setData(null);
            }
            else{
                // 无关联文章则删除
                tagMapper.deleteById(id);
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
