package com.example.blog_common.utils.minio;


import com.example.blog_common.config.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * minio工具类
 * @author palpitate
 * @date 2026/09/09
 */
@Component
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    public MinioUtils(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    /**
     * 上传文件
     * @return 上传结果
     */
    public String upFile(MultipartFile file,String qianzhui) {
        // 判断文件是否为空
        if (file == null || file.getSize() == 0)
            return "not N";
        String fileName = file.getOriginalFilename();
        String houzhui = fileName.substring(fileName.lastIndexOf("."));
        // 判断文件类型
        if (!houzhui.equals(".png") && !houzhui.equals(".jpg") && !houzhui.equals(".webp"))
            return "not T";
        try{
            String key = UUID.randomUUID().toString().replace("-","");

            String objectName = String.format("%s/%s%s",qianzhui, key, houzhui);
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            return objectName;
        }catch (Exception e){
            return "not C";
        }
    }


    /**
     * 获取文件短时访问地址
     * @param objectName 文件名
     * @return String 文件短时访问地址
     */
    public String getObjectUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .expiry(7, TimeUnit.HOURS)
                        .build()
            );
        } catch (Exception e) {
            return "not";
        }
    }
}
