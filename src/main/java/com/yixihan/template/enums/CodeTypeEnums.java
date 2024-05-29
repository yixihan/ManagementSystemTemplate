package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类型
 *
 * @author yixihan
 * @date 2024-05-29 10:55
 */
@Getter
@AllArgsConstructor
public enum CodeTypeEnums {

    REGISTER("REGISTER", "注册"),
    LOGIN("LOGIN", "登录"),
    PASSWORD("PASSWORD", "修改密码"),
    COMMON("COMMON", "通用"),

    ;


    private final String type;

    private final String desc;

}
