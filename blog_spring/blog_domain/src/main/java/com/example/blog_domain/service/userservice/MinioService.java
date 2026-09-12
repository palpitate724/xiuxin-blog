package com.example.blog_domain.service.userservice;

import com.example.blog_common.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Minio服务接口
 * @author palpitate
 * @date 2023/09/04
 */
public interface MinioService {
    /**
     * 上传文件
     * @param file 文件
     * @param qianzhui 前缀
     * @return Result 结果
     */
    Result upFile(MultipartFile file, String qianzhui);

    /**
     * 获取对象url
     * @param objectName 对象名
     * @return Result 对象
     */
    Result getObjectUrl(String objectName);
}
