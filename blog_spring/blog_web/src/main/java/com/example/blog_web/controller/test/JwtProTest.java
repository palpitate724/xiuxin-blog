package com.example.blog_web.controller.test;


import com.example.blog_domain.mapper.ArtTagMapper;
import com.example.blog_domain.service.userservice.impl.SignupUserSerImpl;
import com.example.blog_domain.vo.art.SelectArtVo;
import com.example.blog_domain.vo.arttag.SelectArtTagVo;
import com.example.blog_domain.vo.tag.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * JwtProTest
 * @author palpitate
 * @date 2023/09/04
 */

@RestController
public class JwtProTest {

    private final SignupUserSerImpl signupUserSerImpl;
    private final ArtTagMapper artTagMapper;
    public JwtProTest(SignupUserSerImpl signupUserSerImpl,ArtTagMapper artTagMapper) {
        this.artTagMapper = artTagMapper;
        this.signupUserSerImpl = signupUserSerImpl;
    }



    @GetMapping("/test")
    public Map<Long, SelectArtVo> test() {
        List<Long> artids = List.of(2097580568131289089L, 2097580572833103874L, 2097580577190985730L);
        List<SelectArtTagVo> sav = artTagMapper.selectArtTagList(artids);

        List<SelectArtVo> selectArtVos = artids.stream()
                .map(artid -> {
                    SelectArtVo selectArtVo = new SelectArtVo();
                    selectArtVo.setId(artid);
                    return selectArtVo;
                })
                .toList();

        Map<Long, SelectArtVo> satvMap = selectArtVos.stream()
                .collect(Collectors.toMap(SelectArtVo::getId, selectArtVo -> selectArtVo));

        Map<Long, List<SelectArtTagVo>> artTagMap = sav.stream()
                .collect(Collectors.groupingBy(SelectArtTagVo::getArtid));

        for (Long artid : artids){
            List<SelectArtTagVo> satvl = artTagMap.get(artid);
            List<TagVo> tvl = satvl.stream()
                    .map(selectArtTagVo -> {
                        TagVo tagVo = new TagVo();
                        BeanUtils.copyProperties(selectArtTagVo, tagVo);
                        return tagVo;
                    })
                    .toList();

            satvMap.get(artid).setTagvolist(tvl);
        }
        return satvMap;

    }


}
