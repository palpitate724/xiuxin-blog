package com.example.blog_domain.service.artservice.impl;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.mapper.ArtMapper;
import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.service.artservice.DeleteArtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 删除文章服务实现类
 * @author palpitate
 * @date 2026/09/10
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class DeleteArtSerImpl implements DeleteArtService {

    private final ArtMapper artMapper;
    private final ArtTagMapper artTagMapper;
    public DeleteArtSerImpl(ArtMapper artMapper, ArtTagMapper artTagMapper) {
        this.artMapper = artMapper;
        this.artTagMapper = artTagMapper;
    }

    @Override
    public Result deleteArt(Long artid) {
        Result result = Result.getInstance();

//        try{
            //删除文章关联标签
            artTagMapper.deleteById(artid);
            //删除文章
            artMapper.deleteById(artid);

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(null);

//        }catch (Exception e){
//            result.setCode(ResultCode.ARTICLE_DELETED.getCode());
//            result.setMessage(ResultCode.ARTICLE_DELETED.getMessage());
//            result.setData(null);
//        }

        return result;
    }
}
