package com.example.blog_web.controller.artcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.artservice.SelectArtPageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SelectArtListCon 类
 * @author palpitate
 * @date 2026/09/09
 */
@RestController
@RequestMapping("/api/v1/art")
public class SelectArtPageCon {

    private final SelectArtPageService selectArtPageService;
    public SelectArtPageCon(SelectArtPageService selectArtPageService) {
        this.selectArtPageService = selectArtPageService;
    }


    /**
     * 查询文章列表
     * @return
     */
    @GetMapping
    public Result selectArtList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size){
        return selectArtPageService.selectArtPage(page, size);
    }
}
