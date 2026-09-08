package com.example.blog_domain.service.catservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.cat.AddCatDto;
import com.example.blog_domain.entity.CatEntity;
import com.example.blog_domain.mapper.CatMapper;
import com.example.blog_domain.service.catservice.AddCatService;
import com.example.blog_domain.vo.cat.SelectCatVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;


/**
 * 添加分类服务实现类
 * @author palpitate
 * @date 2023/09/05
 */
@Slf4j
@Service
public class AddCatSerImpl implements AddCatService {

    private final CatMapper catMapper;
    public AddCatSerImpl(CatMapper catMapper) {
        this.catMapper = catMapper;
    }

    @Override
    public Result addCat(AddCatDto acd) {
        Result result = Result.getInstance();

        // 分类名存在，返回错误
        if (catMapper.selectByMap(Map.of("catname", acd.getCatname())) == null){
            result.setCode(ResultCode.CATEGORY_ALREADY_EXIST.getCode());
            result.setMessage(ResultCode.CATEGORY_ALREADY_EXIST.getMessage());
            result.setData(null);
        }
        // 分类名不存在，插入分类
        else{
            CatEntity ce = new CatEntity();
            BeanUtils.copyProperties(acd, ce);
            catMapper.insert(ce);
            SelectCatVo scv = new SelectCatVo();
            BeanUtils.copyProperties(ce, scv);

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(scv);
        }

        return result;
    }

}
