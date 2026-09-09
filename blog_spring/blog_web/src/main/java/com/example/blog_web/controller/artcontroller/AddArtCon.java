package com.example.blog_web.controller.artcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.art.AddArtDto;
import com.example.blog_domain.service.artservice.AddArtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加文章控制器
 * @author palpitate
 * @date 2026/09/09
 */
@RestController
@RequestMapping("/api/v1/art")
public class AddArtCon {

    private final AddArtService addArtService;
    private AddArtCon (AddArtService addArtService){
        this.addArtService = addArtService;
    }


    @PostMapping
    public Result addArt(@RequestBody AddArtDto aad){
        System.out.println(aad);
        return addArtService.addArt(aad);
    }
}
