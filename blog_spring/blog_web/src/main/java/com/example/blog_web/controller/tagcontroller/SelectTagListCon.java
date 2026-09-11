package com.example.blog_web.controller.tagcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.tagservice.SelectTagListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询标签列表控制层
 * @author palpitate
 * @date 2026/09/09
 */
@RestController
@RequestMapping("/api/v1/{id}/tag")
public class SelectTagListCon {

    private SelectTagListService selectTagListService;
    public SelectTagListCon(SelectTagListService selectTagListService) {
        this.selectTagListService = selectTagListService;
    }

    /**
     * 查询标签列表
     * @return
     */
    @GetMapping
    public Result selectTagList() {
        return selectTagListService.selectTagList();
    }
}
