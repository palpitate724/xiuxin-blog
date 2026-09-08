package com.example.blog_web.controller.tagcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.tag.AddTagDto;
import com.example.blog_domain.service.tagservice.AddTagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加标签控制器
 * @author palpitate
 * @date 2023/09/05
 */
@RestController
@RequestMapping("/api/v1/{id}/tag")
public class AddTagCon {

    private final AddTagService addTagService;
    public AddTagCon(AddTagService addTagService) {
        this.addTagService = addTagService;
    }


    @PostMapping
    public Result addTag(@RequestBody AddTagDto atd) {
        return addTagService.addTag(atd);
    }

}
