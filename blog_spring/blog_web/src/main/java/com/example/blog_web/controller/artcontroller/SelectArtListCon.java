package com.example.blog_web.controller.artcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.artservice.SelectArtListService;
import com.example.blog_web.config.Log;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SelectArtListCon 类
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@RestController
@RequestMapping("/api/v1/art")
public class SelectArtListCon {

    private final SelectArtListService selectArtListService;
    public SelectArtListCon(SelectArtListService selectArtListService) {
        this.selectArtListService = selectArtListService;
    }


    /**
     * 查询文章列表
     * @return
     */
    @GetMapping
    public Result selectArtList(){
        return selectArtListService.selectArtList();
    }
}
