package com.example.blog_web.controller.artcontroller;

import com.example.blog_common.result.Result;
import com.example.blog_domain.service.artservice.DeleteArtService;
import com.example.blog_web.config.Log;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 删除文章控制器
 * @author palpitate
 * @date 2026/09/10
 */
@Transactional(rollbackFor = Exception.class)
@RestController
@RequestMapping("/api/v1/art/")
public class DeleteArtCon {

    private final DeleteArtService deleteArtService;
    public DeleteArtCon(DeleteArtService deleteArtService) {
        this.deleteArtService = deleteArtService;
    }


    /**
     * 删除文章
     * @param artid
     * @return
     */
    @DeleteMapping("/{artid}")
    public Result deleteArt(@PathVariable Long artid) {
        return deleteArtService.deleteArt(artid);
    }
}
