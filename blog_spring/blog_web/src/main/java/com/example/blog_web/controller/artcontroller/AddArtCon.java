package com.example.blog_web.controller.artcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.art.AddArtDto;
import com.example.blog_domain.service.artservice.AddArtService;
import com.example.blog_web.config.Log;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加文章控制器
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@RestController
@RequestMapping("/api/v1/art")
public class AddArtCon {

    private final AddArtService addArtService;
    public AddArtCon (AddArtService addArtService){
        this.addArtService = addArtService;
    }

    /**
     * 添加文章
     * @param addArtDto
     * @return
     */
    @PostMapping
    public Result addArt(@RequestBody AddArtDto addArtDto){
        System.out.println(addArtDto);
        return addArtService.addArt(addArtDto);
    }
}
