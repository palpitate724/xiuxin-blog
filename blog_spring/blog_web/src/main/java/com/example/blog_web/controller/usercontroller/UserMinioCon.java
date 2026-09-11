package com.example.blog_web.controller.usercontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.userservice.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户Minio控制器
 * @author palpitate
 * @date 2026/09/09
 */
@RestController
@RequestMapping("/api/v1/user/{id}/minio")
public class UserMinioCon {

    private final MinioService MinioService;
    public UserMinioCon(MinioService MinioService) {
        this.MinioService = MinioService;
    }

    /**
     * 上传用户头像文件
     * @param file
     * @return
     */
    @PostMapping
    public Result upFile(@RequestBody MultipartFile file) {
        return MinioService.upFile(file, "user");
    }

    /**
     * 获取临时用户头像url
     * @param objectName
     * @return
     */
    @GetMapping
    public Result getObjectUrl(@PathVariable("id") String objectName) {
        return MinioService.getObjectUrl(objectName);
    }
}
