package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author yixihan
 * @date 2024-05-21 15:09
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnums {

    VALID("valid", "有效的"),
    INVALID("invalid", "无效的"),

    ;

    private final String uiCode;

    private final String desc;
    }
