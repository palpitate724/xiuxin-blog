package com.example.blog_common.enums;

import lombok.Getter;

/**
 * 存储类型枚举
 * 定义文件存储的各种类型
 */
@Getter
public enum StorageType {
    
    /**
     * 本地存储
     */
    LOCAL(0, "本地存储"),
    
    /**
     * MinIO存储
     */
    MINIO(1, "MinIO存储"),
    
    /**
     * 阿里云OSS
     */
    OSS(2, "阿里云OSS"),
    
    /**
     * 腾讯云COS
     */
    COS(3, "腾讯云COS"),
    
    /**
     * 七牛云
     */
    QINIU(4, "七牛云");

    private final Integer code;
    private final String desc;

    StorageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据存储类型码获取枚举
     * @param code 存储类型码
     * @return 对应的枚举值
     */
    public static StorageType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (StorageType type : StorageType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
