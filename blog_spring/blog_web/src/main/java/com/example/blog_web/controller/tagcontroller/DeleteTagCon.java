package com.example.blog_web.controller.tagcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.tagservice.DeleteTagService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 删除标签控制器
 * @author palpitate
 * @date 2026/11/08
 */
@RestController
@RequestMapping("/api/v1/tag")
public class DeleteTagCon {

    private final DeleteTagService deleteTagService;
    public DeleteTagCon(DeleteTagService deleteTagService) {
        this.deleteTagService = deleteTagService;
    }

    /**
     * 删除标签
      * @param id 标签id
     * @return Result
     */
    @DeleteMapping("/{id}")
    public Result deleteTag(@PathVariable Long id){
        return deleteTagService.deleteTag(id);
    }


}
