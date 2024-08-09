package com.yixihan.template.common.enums;

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
    TOKEN_EXPIRED(20003, "token 过期"),
    NO_METHOD_ROLE(20004, "没有访问权限"),
    ACCESS_DENIED(20005, "拒绝访问"),
    ACCOUNT_NOT_FOUND(20006, "找不到该账号"),
    PASSWORD_ERR(20007, "密码错误"),
    CODE_EXPIRE_ERR(20008, "验证码过期"),
    CODE_VALIDATE_ERROR(20009, "验证码校验错误"),


    // ====================== Third Exception ======================
    PICTURE_CODE_ERR(30001, "图片验证码生成异常"),
    PICTURE_QR_ERR(30002, "二维码生成异常"),
    EMAIL_SEND_ERR(30003, "邮件发送失败"),
    SMS_SEND_ERR(30004, "短信发送失败")

    ;


    private final Integer code;

    private final String message;
}
