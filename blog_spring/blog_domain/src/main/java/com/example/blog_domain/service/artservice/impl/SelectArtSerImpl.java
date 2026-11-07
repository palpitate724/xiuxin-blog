package com.example.blog_domain.service.artservice.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_common.utils.minio.MinioUtils;
import com.example.blog_domain.entity.*;
import com.example.blog_domain.mapper.*;
import com.example.blog_domain.service.artservice.SelectArtService;
import com.example.blog_domain.vo.art.SelectArtByIdVo;
import com.example.blog_domain.vo.cat.SelectCatVo;
import com.example.blog_domain.vo.tag.TagVo;
import com.example.blog_domain.vo.user.ArtUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectArtSerImpl 类
 * @author palpitate
 * @date 2026/09/18
 */
@Service
public class SelectArtSerImpl implements SelectArtService {

    private final TagMapper tagMapper;
    private final ArtMapper artMapper;
    private final ArtTagMapper artTagMapper;
    private final MinioUtils minioUtils;
    private final UserMapper userMapper;
    private final CatMapper catMapper;

    public SelectArtSerImpl(ArtMapper artMapper, ArtTagMapper artTagMapper, MinioUtils minioUtils, TagMapper tagMapper, UserMapper userMapper, CatMapper catMapper) {
        this.artMapper = artMapper;
        this.userMapper = userMapper;
        this.minioUtils = minioUtils;
        this.artTagMapper = artTagMapper;
        this.tagMapper = tagMapper;
        this.catMapper = catMapper;
    }

    @Override
    public Result selectArtById(Long id) {
        Result result = Result.getInstance();

        try{
            //优化目标：根据id查询文章，单次查询全部
            // 创建文章返回vo
            SelectArtByIdVo sabiv = new SelectArtByIdVo();

            // 根据id查询文章
            ArtEntity ae = artMapper.selectById(id);

            //根据userid查询user数据并组装
            UserEntity ue = userMapper.selectById(ae.getUserid());
            ArtUserVo auv = new ArtUserVo();
            BeanUtils.copyProperties(ue, auv);
            auv.setId(ue.getId().toString());
            sabiv.setUservo(auv);

            //根据catid查询cat数据并组装
            CatEntity ce = catMapper.selectById(ae.getCatid());
            SelectCatVo scv = new SelectCatVo();
            BeanUtils.copyProperties(ce, scv);
            scv.setId(ce.getId().toString());
            sabiv.setCatvo(scv);

            // 根据文章id查询标签列表
            List<ArtTagEntity> atel = artTagMapper.selectList(
                    new LambdaQueryWrapper<ArtTagEntity>()
                            .eq(ArtTagEntity::getArtid, id)
            );
            // 组装标签id列表，用于后续查询标签详情
            List<Long> tagIdList = atel.stream()
                    .map(ArtTagEntity::getTagid)
                    .toList();




            // 获取封面临时访问url
            sabiv.setFenmianurl(minioUtils.getObjectUrl(ae.getObjectname()));
            BeanUtils.copyProperties(ae, sabiv);
            sabiv.setId(ae.getId().toString());


            // 根据标签id列表查询标签详情
            if (!tagIdList.isEmpty()){
                List<TagEntity> tel = tagMapper.selectByIds(tagIdList);
                System.out.println(tel);
                // 组装标签列表
                List<TagVo> tvl = new ArrayList<>();
                for (TagEntity te : tel) {
                    TagVo tv = new TagVo();
                    BeanUtils.copyProperties(te, tv);
                    tv.setId(te.getId().toString());
                    tvl.add(tv);
                }
                sabiv.setTagvolist(tvl);
            }

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
