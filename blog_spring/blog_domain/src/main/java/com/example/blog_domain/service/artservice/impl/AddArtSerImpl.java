package com.example.blog_domain.service.artservice.impl;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.art.AddArtDto;
import com.example.blog_domain.entity.ArtEntity;
import com.example.blog_domain.entity.ArtTagEntity;
import com.example.blog_domain.mapper.ArtMapper;
import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.service.artservice.AddArtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * AddArtSerImpl 类
 * @author palpitate
 * @date 2026/09/09
 */
@Service
@Slf4j
public class AddArtSerImpl implements AddArtService {


    private final ArtMapper artMapper;
    private final ArtTagMapper artTagMapper;
    public AddArtSerImpl(ArtMapper artMapper, ArtTagMapper artTagMapper) {
        this.artTagMapper = artTagMapper;
        this.artMapper = artMapper;
    }

    /**
     * 添加文章
     * @param aad 添加文章的DTO
     * @return Result 添加文章的结果
     */
    @Override
    public Result addArt(AddArtDto aad) {
        Result result = Result.getInstance();

        try{
            ArtEntity ae = new ArtEntity();
            BeanUtils.copyProperties(aad, ae);

            // 插入文章
            artMapper.insert(ae);

            List<ArtTagEntity> atel = new ArrayList<>();
            List<Long> tagids = aad.getTagids();
            for (Long tagid : tagids) {
                ArtTagEntity ate = new ArtTagEntity();
                ate.setArtid(ae.getId());
                ate.setTagid(tagid);
                atel.add(ate);
            }

            // 插入文章标签
            artTagMapper.insert(atel);

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(ae.getId());
        }catch (Exception e){
            result.setCode(ResultCode.ARTICLE_NOT_INSERT.getCode());
            result.setMessage(ResultCode.ARTICLE_NOT_INSERT.getMessage());
            result.setData(null);
        }

        return result;
    }
}
