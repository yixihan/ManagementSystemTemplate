package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 认证异常
 *
 * @author yixihan
 * @date 2024-05-21 15:15
 */
@SuppressWarnings("unused")
public class AuthException extends BizException {

    @Serial
    private static final long serialVersionUID = -2690717359446057710L;

    public AuthException() {
        this(ExceptionEnums.AUTH_ERR);
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(ExceptionEnums enums) {
        super(enums);
    }

    public AuthException(Throwable e) {
        super(e);
    }
}
