package com.example.blog_web.controller.artcontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.service.userservice.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * ArtMinioCon
 * @author palpitate
 * @date 2023/09/05
 */
@RestController
@RequestMapping("/api/v1/art/{id}/minio")
public class ArtMinioCon {


    private final MinioService minioService;
    public ArtMinioCon(MinioService minioService) {
        this.minioService = minioService;
    }

    /**
     * 上传文章封面文件
     * @param file 文件
     * @return Result 对象
     */
    @PostMapping
    public Result upFile(@RequestBody MultipartFile file) {
        return minioService.upFile(file, "art");
    }

    /**
     * 获取临时文章封面文件url
     * @param objectName 对象名
     * @return Result 对象
     */
    @GetMapping
    public Result getObjectUrl(@PathVariable("id") String objectName) {
        return minioService.getObjectUrl(objectName);
    }
}
