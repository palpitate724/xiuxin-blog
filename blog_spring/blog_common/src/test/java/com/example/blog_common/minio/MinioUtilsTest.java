package com.example.blog_common.minio;


import com.example.blog_common.utils.minio.MinioUtils;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * minio工具类测试
 */
@SpringBootTest
public class MinioUtilsTest {

    @Autowired
    private MinioUtils minioUtils;


    /**
     * 测试uuid
     */
    @Test
    public void uuidTest(){
        String key = UUID.randomUUID().toString().replace("-","");
        String houzhui = ".jpg";
        String objectName = String.format("%s/%s%s","user", key, houzhui);
        System.out.println(objectName);
    }
    /**
     * 测试获取对象url
     */
    @Test
    public void getObjectUrlTest() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        System.out.println(minioUtils.getObjectUrl("user/usertoux.png"));
    }
}
