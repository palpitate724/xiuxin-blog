package com.example.blog_web.controller.tagcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.tag.AddTagDto;
import com.example.blog_domain.service.tagservice.AddTagService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加标签控制器
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@RestController
@RequestMapping("/api/v1/{id}/tag")
public class AddTagCon {

    private final AddTagService addTagService;
    public AddTagCon(AddTagService addTagService) {
        this.addTagService = addTagService;
    }


    /**
     * 添加标签
     * @param atd
     * @return
     */
    @PostMapping
    public Result addTag(@RequestBody AddTagDto atd) {
        return addTagService.addTag(atd);
    }

}
