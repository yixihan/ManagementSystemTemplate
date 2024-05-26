package com.yixihan.template.enums;

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

    PENDING("Pending", "准备中"),
    RUNNING("Running", "执行中"),
    FAILED("Failed", "失败"),
    SUCCESS("Success", "成功"),

    ;


    private final String uiStatus;

    private final String desc;
}
