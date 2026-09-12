package com.example.blog_domain.service.tagservice.impl;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.entity.TagEntity;
import com.example.blog_domain.mapper.TagMapper;
import com.example.blog_domain.service.tagservice.SelectTagListService;
import com.example.blog_domain.vo.tag.SelectTagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 查询标签列表服务实现类
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SelectTagListSerImpl implements SelectTagListService {

    private final TagMapper tagMapper;
    public SelectTagListSerImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public Result selectTagList(){

        Result result = Result.getInstance();

        List<TagEntity> tagEntityList = tagMapper.selectList(null);

        List<SelectTagVo> selectTagListVoList = tagEntityList.stream()
                .map(tagEntity -> {
                    SelectTagVo selectTagListVo = new SelectTagVo();
                    BeanUtils.copyProperties(tagEntity, selectTagListVo);
                    return selectTagListVo;
                })
                .toList();

        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(selectTagListVoList);
        return result;
    }
}
