package com.example.blog_web.controller.catcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.catservice.SelectCatListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询分类列表控制器
 * @author palpitate
 * @date 2023/09/05
 */
@RestController
@RequestMapping("/api/v1/{id}/cat")
public class SelectCatListCon {

    private final SelectCatListService selectCatListService;
    public SelectCatListCon(SelectCatListService selectCatListService) {
        this.selectCatListService = selectCatListService;
    }


    @GetMapping
    public Result selectCatList() {
        return selectCatListService.selectListCat();
    }
}
