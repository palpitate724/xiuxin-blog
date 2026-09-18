package com.example.blog_domain.service.artservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_common.utils.minio.MinioUtils;
import com.example.blog_domain.entity.ArtEntity;
import com.example.blog_domain.mapper.ArtMapper;
import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.service.artservice.SelectArtListService;
import com.example.blog_domain.vo.art.SelectArtVo;
import com.example.blog_domain.vo.arttag.SelectArtTagVo;
import com.example.blog_domain.vo.tag.TagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * SelectArtListImpl 类
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class SelectArtListImpl implements SelectArtListService {

    private final ArtMapper artMapper;
    private final ArtTagMapper artTagMapper;
    private final MinioUtils minioUtils;
    public SelectArtListImpl(ArtMapper artMapper, ArtTagMapper artTagMapper, MinioUtils minioUtils) {
        this.artMapper = artMapper;
        this.artTagMapper = artTagMapper;
        this.minioUtils = minioUtils;
    }

    @Override
    public Result selectArtList() {
        Result result = Result.getInstance();

        try{
            //查询所有文章
            List<ArtEntity> ae = artMapper.selectList(
                    new LambdaQueryWrapper<ArtEntity>()
                            .select(ArtEntity::getId, ArtEntity::getName, ArtEntity::getUserid, ArtEntity::getCatid, ArtEntity::getObjectname, ArtEntity::getSum, ArtEntity::getCjiantime)
                            .eq(ArtEntity::getDeleted, 0)
            );

            //为所有查询到的文章申请临封面url
            ae.forEach(ArtEntity -> {
                ArtEntity.setObjectname(minioUtils.getObjectUrl(ArtEntity.getObjectname()));
            });

            //将查询结果转为SelectArtListVo list
            List<SelectArtVo> salv = ae.stream()
                    .map(ArtEntity -> {
                        SelectArtVo selectArtVo = new SelectArtVo();
                        BeanUtils.copyProperties(ArtEntity, selectArtVo);
                        selectArtVo.setFenmianurl(ArtEntity.getObjectname());
                        return selectArtVo;
                    })
                    .toList();

            //收集ArtEntity list中的id
            List<Long> artids = ae.stream()
                    .map(ArtEntity::getId)
                    .toList();

            //将SelectArtListVo list转为Map
            Map<Long, SelectArtVo> satlv = salv.stream()
                    .collect(Collectors.toMap(SelectArtVo::getId, selectArtVo -> selectArtVo));

            //根据id查询ArtTagEntity list
            List<SelectArtTagVo> satvl = artTagMapper.selectArtTagList(artids);
            System.out.println("satvl: " + satvl);

            //将ArtTagEntity list转为Map
            Map<Long, List<SelectArtTagVo>> satvlmap = satvl.stream()
                    .collect(Collectors.groupingBy(SelectArtTagVo::getArtid));

            //将tag标签列表组装进selectartlistvo
            for (Long artid : artids){
                //将map转为list
                List<TagVo> tvl = satvlmap.get(artid).stream()
                        .map(selectArtTagVo -> {
                            TagVo tagVo = new TagVo();
                            BeanUtils.copyProperties(selectArtTagVo, tagVo);
                            return tagVo;
                        })
                        .toList();
                satlv.get(artid).setTagvolist(tvl);
            }

            //将Map结果转为List
            List<SelectArtVo> satlvlist = satlv.values().stream()
                    .toList();

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(satlvlist);
        }catch (Exception e){
            result.setCode(ResultCode.FAIL.getCode());
            result.setMessage(ResultCode.FAIL.getMessage());
            result.setData(null);
        }

        return result;
    }
}
