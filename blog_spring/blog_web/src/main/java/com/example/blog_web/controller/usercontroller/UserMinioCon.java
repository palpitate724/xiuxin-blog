package com.example.blog_web.controller.usercontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.userservice.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户Minio控制器
 * @author palpitate
 * @date 2023/09/04
 */
@RestController
@RequestMapping("/api/v1/user/{id}/minio")
public class UserMinioCon {

    private final MinioService minioService;
    public UserMinioCon(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    public Result upFile(@RequestBody MultipartFile file) {
        return minioService.upFile(file, "user");
    }

    @GetMapping
    public Result getObjectUrl(@PathVariable("id") String objectName) {
        return minioService.getObjectUrl(objectName);
    }
}
