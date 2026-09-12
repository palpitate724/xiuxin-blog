package com.example.blog_web.controller.catcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.cat.AddCatDto;
import com.example.blog_domain.service.catservice.AddCatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加分类控制器
 * @author palpitate
 * @date 2026/09/09
 */
@RestController
@RequestMapping("/api/v1/{id}/cat")
public class AddCatCon {

    private final AddCatService addCatService;
    public AddCatCon(AddCatService addCatService) {
        this.addCatService = addCatService;
    }

    /**
     * 添加分类
     * @param acd 分类数据传输对象
     * @return Result 结果
     */
    @PostMapping
    public Result addCat(@RequestBody AddCatDto acd) {
        return addCatService.addCat(acd);
    }
}
