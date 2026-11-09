package com.example.blog_web.controller.catcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.catservice.DeleteCatService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 删除分类控制器
 * @author palpitate
 * @date 2026/11/09
 */
@RestController
@RequestMapping("/api/v1/cat")
public class DeleteCatCon {

    private final DeleteCatService deleteCatService;
    public DeleteCatCon(DeleteCatService deleteCatService) {
        this.deleteCatService = deleteCatService;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteCat(@PathVariable Long id) {
        return deleteCatService.deleteCat(id);
    }

}
