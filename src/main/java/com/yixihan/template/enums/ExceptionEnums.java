package com.yixihan.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 *
 * @author yixihan
 * @date 2024-05-21 14:56
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnums {

    // ====================== System Exception ======================
    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION(500, "系统异常"),

    /**
     * 错误请求
     */
    FAILED_TYPE_BAD_REQUEST(400, "错误请求"),

    /**
     * 未授权
     */
    FAILED_TYPE_UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FAILED_TYPE_FORBIDDEN(403, "禁止访问"),

    /**
     * 业务异常 (默认)
     */
    FAILED_TYPE_BUSINESS(10001, "业务异常"),

    /**
     * 参数校验异常
     */
    PARAMS_VALID_ERR(10002, "参数校验异常"),

    /**
     * 空指针异常
     */
    NULL_ERR(10003, "空指针异常"),



    // ====================== Auth Exception ======================
    AUTH_ERR(20001, "认证异常"),
    TOKEN_ERR(20002, "token 解析异常"),


    ;


    private final Integer code;

    private final String message;
}
