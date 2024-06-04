package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储方式 枚举
 *
 * @author yixihan
 * @date 2024-06-04 13:42
 */
@Getter
@AllArgsConstructor
public enum OsTypeEnums {

    LOCAL("LOCAL", "本地存储"),

    DB("DB", "数据库存储"),

    OSS("OSS", "阿里云 OSS 存储"),

    COS("COS", "腾讯云 COS 存储"),

    KODO("KODO", "七牛云 Kodo 存储"),

    SMMS("SMMS", "SM.MS 存储")

    ;

    private final String value;

    private final String desc;
}
