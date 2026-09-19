package com.example.blog_domain.service.artservice.impl;


import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_common.utils.minio.MinioUtils;
import com.example.blog_domain.entity.ArtEntity;
import com.example.blog_domain.entity.ArtTagEntity;
import com.example.blog_domain.entity.TagEntity;
import com.example.blog_domain.mapper.ArtMapper;
import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.mapper.TagMapper;
import com.example.blog_domain.service.artservice.SelectArtService;
import com.example.blog_domain.vo.art.SelectArtByIdVo;
import com.example.blog_domain.vo.art.SelectArtVo;
import com.example.blog_domain.vo.arttag.SelectArtTagVo;
import com.example.blog_domain.vo.tag.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SelectArtSerImpl 类
 * @author palpitate
 * @date 2026/09/18
 */
@Service
public class SelectArtSerImpl implements SelectArtService {

    private final TagMapper tagMapper;
    private ArtMapper artMapper;
    private ArtTagMapper artTagMapper;
    private MinioUtils minioUtils;
    public SelectArtSerImpl(ArtMapper artMapper, ArtTagMapper artTagMapper, MinioUtils minioUtils, TagMapper tagMapper) {
        this.artMapper = artMapper;
        this.minioUtils = minioUtils;
        this.artTagMapper = artTagMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public Result selectArtById(Long id) {
        Result result = Result.getInstance();

        try{

            // 根据id查询文章
            ArtEntity ae = artMapper.selectById(id);
            // 根据文章id查询标签列表
            List<ArtTagEntity> atel = artTagMapper.selectList(
                    new LambdaQueryWrapper<ArtTagEntity>()
                            .eq(ArtTagEntity::getArtid, id)
            );
            // 组装标签id列表，用于后续查询标签详情
            List<Long> tagIdList = atel.stream()
                    .map(ArtTagEntity::getTagid)
                    .toList();
            // 根据标签id列表查询标签详情
            List<TagEntity> tel = tagMapper.selectByIds(tagIdList);
            System.out.println(tel);

            // 组装完整文章返回vo
            SelectArtByIdVo sabiv = new SelectArtByIdVo();
            // 获取封面临时访问url
            sabiv.setFenmianurl(minioUtils.getObjectUrl(ae.getObjectname()));
            BeanUtils.copyProperties(ae, sabiv);

            // 组装标签列表
            List<TagVo> tvl = new ArrayList<>();
            for (TagEntity te : tel) {
                TagVo tv = new TagVo();
                BeanUtils.copyProperties(te, tv);
                tvl.add(tv);
            }
            sabiv.setTagvolist(tvl);

            // 设置结果
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(sabiv);

        }catch (Exception e){ // 捕获异常处理
            result.setCode(ResultCode.FAIL.getCode());
            result.setMessage(ResultCode.FAIL.getMessage());
            result.setData(null);
        }

        return result;


    }
}
