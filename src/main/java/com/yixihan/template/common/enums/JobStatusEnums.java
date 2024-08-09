package com.yixihan.template.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * job status 枚举
 *
 * @author yixihan
 * @date 2024-05-26 09:39
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnums {

    PENDING("PENDING", "准备中"),
    RUNNING("RUNNING", "执行中"),
    FAILED("FAILED", "失败"),
    SUCCESS("SUCCESS", "成功"),

    ;


    private final String status;

    private final String desc;
}
