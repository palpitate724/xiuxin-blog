package com.example.blog_domain.service.catservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_domain.entity.CatEntity;
import com.example.blog_domain.mapper.CatMapper;
import com.example.blog_domain.service.catservice.SelectCatListService;
import com.example.blog_domain.vo.cat.SelectCatVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 查询分类列表服务实现类
 * @author palpitate
 * @date 2023/09/05
 */
@Service
public class SelectCatListSerImpl implements SelectCatListService {

    private final CatMapper catMapper;
    public SelectCatListSerImpl(CatMapper catMapper) {
        this.catMapper = catMapper;
    }

    /**
     * 查询分类列表
     * @return
     */
    @Override
    public Result selectListCat() {

        List<CatEntity> selectCatEntityList = catMapper.selectList(null);
        List<SelectCatVo> selectCatVoList = selectCatEntityList.stream()
                .map(selectCatEntity -> {
                    SelectCatVo selectCatVo = new SelectCatVo();
                    BeanUtils.copyProperties(selectCatEntity, selectCatVo);
                    return selectCatVo;
                })
                .toList();
        Result result = Result.getInstance();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(selectCatVoList);
        return result;
    }
}
