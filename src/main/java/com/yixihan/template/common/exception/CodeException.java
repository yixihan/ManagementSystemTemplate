package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 验证码异常
 *
 * @author yixihan
 * @date 2024-05-29 09:54
 */
@SuppressWarnings("unused")
public class CodeException extends BizException {

    @Serial
    private static final long serialVersionUID = -6335813997590100318L;

    public CodeException() {
        super(ExceptionEnums.CODE_VALIDATE_ERROR);
    }

    public CodeException(String message) {
        super(message);
    }

    public CodeException(Throwable e) {
        super(e);
    }

    public CodeException(ExceptionEnums enums) {
        super(enums);
    }
}
