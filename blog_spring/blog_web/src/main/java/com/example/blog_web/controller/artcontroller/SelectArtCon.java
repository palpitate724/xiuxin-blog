package com.example.blog_web.controller.artcontroller;

import com.example.blog_common.result.Result;
import com.example.blog_domain.service.artservice.SelectArtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SelectArtCon 类
 * @author palpitate
 * @date 2026/09/18
 */
@RestController
@RequestMapping("/api/v1/art")
public class SelectArtCon {

    private SelectArtService selectArtService;
    public SelectArtCon(SelectArtService selectArtService) {
        this.selectArtService = selectArtService;
    }


    @GetMapping("/{id}")
    public Result selectArtById(@PathVariable Long id) {
        return selectArtService.selectArtById(id);
    }

}
