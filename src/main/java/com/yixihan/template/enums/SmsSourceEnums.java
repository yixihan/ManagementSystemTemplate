package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件源 枚举
 *
 * @author yixihan
 * @date 2024-05-29 14:26
 */
@Getter
@AllArgsConstructor
public enum SmsSourceEnums {

    AL("AL", "阿里云"),
    TENCENT("TENCENT", "腾讯云"),

    ;

    private final String value;

    private final String desc;
}
