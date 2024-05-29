package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description
 *
 * @author yixihan
 * @date 2024-05-28 14:23
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnums {

    PASSWORD("PASSWORD", "密码登录"),
    EMAIL("EMAIL", "邮件登录"),
    MOBILE("MOBILE", "手机号登录"),

    ;

    private final String type;

    private final String desc;
}