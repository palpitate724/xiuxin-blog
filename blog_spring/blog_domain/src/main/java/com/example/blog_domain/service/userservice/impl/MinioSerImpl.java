package com.example.blog_domain.service.userservice.impl;


import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_common.utils.minio.MinioUtils;
import com.example.blog_domain.service.userservice.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Minio服务实现类
 * @author palpitate
 * @date 2023/09/04
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class MinioSerImpl implements MinioService {

    private final MinioUtils minioUtils;
    public MinioSerImpl(MinioUtils minioUtils) {
        this.minioUtils = minioUtils;
    }

    /**
     * 上传文件
     * @param file 文件
     * @return Result 对象
     */
    @Override
    public Result upFile(MultipartFile file,String qianzhui) {
        log.info("开始上传文件：{}", file.getOriginalFilename());
        String objectname = minioUtils.upFile(file,qianzhui);
        Result result = Result.getInstance();

        // 判断文件名是否为空
        if (objectname.equals("not N")){
            result.setCode(ResultCode.PARAM_EMPTY.getCode());
            result.setMessage(ResultCode.PARAM_EMPTY.getMessage());
            result.setData(null);
        }
        // 判断文件类型是否支持
        else if (objectname.equals("not T")){
            result.setCode(ResultCode.FILE_TYPE_NOT_SUPPORTED.getCode());
            result.setMessage(ResultCode.FILE_TYPE_NOT_SUPPORTED.getMessage());
            result.setData(null);
        }
        // 判断文件上传是否成功
        else if (objectname.equals("not C")){
            result.setCode(ResultCode.FILE_UPLOAD_FAILED.getCode());
            result.setMessage(ResultCode.FILE_UPLOAD_FAILED.getMessage());
            result.setData(null);
        }
        // 文件上传成功
        else {
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(objectname);
        }
        return result;
    }

    /**
     * 获取头像url
     * @param objectName 对象名
     * @return Result 对象
     */
    @Override
    public Result getObjectUrl(String objectName){
        String url = minioUtils.getObjectUrl(objectName);
        Result result = Result.getInstance();
        // 判断文件名是否为空
        if (url.equals("not")){
            result.setCode(ResultCode.RESOURCE_REQUEST_FAILED.getCode());
            result.setMessage(ResultCode.RESOURCE_REQUEST_FAILED.getMessage());
            result.setData(null);
        }
        // 文件名不为空
        else {
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(url);
        }
        return result;
    }
}
