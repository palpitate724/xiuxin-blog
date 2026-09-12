package com.example.blog_domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog_domain.entity.ArtTagEntity;
import com.example.blog_domain.vo.arttag.SelectArtTagVo;

import java.util.List;


/**
 * ArtTagMapper.xml 类
 * @author palpitate
 * @date 2026/09/09
 */
public interface ArtTagMapper extends BaseMapper<ArtTagEntity> {

    List<SelectArtTagVo> selectArtTagList(List<Long> artids);
}
