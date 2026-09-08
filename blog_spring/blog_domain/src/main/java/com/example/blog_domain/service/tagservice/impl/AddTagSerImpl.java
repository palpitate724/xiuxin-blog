package com.example.blog_domain.service.tagservice.impl;


import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.tag.AddTagDto;
import com.example.blog_domain.entity.TagEntity;
import com.example.blog_domain.mapper.TagMapper;
import com.example.blog_domain.service.tagservice.AddTagService;
import com.example.blog_domain.vo.tag.SelectTagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 添加标签服务实现类
 * @author palpitate
 * @date 2023/09/05
 */
@Service
public class AddTagSerImpl implements AddTagService {

    private final TagMapper tagMapper;
    public AddTagSerImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }


    @Override
    public Result addTag(AddTagDto atd){

        Result result = Result.getInstance();

        // 判断标签是否已存在
        if (tagMapper.selectByMap(Map.of("tagname", atd.getTagname())) == null){
            result.setCode(ResultCode.TAG_ALREADY_EXIST.getCode());
            result.setMessage(ResultCode.TAG_ALREADY_EXIST.getMessage());
            result.setData(null);
            return result;
        }
        // 标签不存在，添加标签
        else {
            TagEntity te = new TagEntity();
            BeanUtils.copyProperties(atd, te);
            tagMapper.insert(te);
            SelectTagVo stv = new SelectTagVo();
            BeanUtils.copyProperties(te, stv);

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(stv);
        }

        return result;
    }

}
