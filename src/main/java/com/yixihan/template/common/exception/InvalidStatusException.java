package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 状态校验异常
 *
 * @author yixihan
 * @date 2024-05-27 09:56
 */
@SuppressWarnings("unused")
public class InvalidStatusException extends BizException{

    @Serial
    private static final long serialVersionUID = -3020266562817335135L;

    public InvalidStatusException() {
        super(ExceptionEnums.INVALID_CODE_ERR);
    }

    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(Throwable e) {
        super(e);
    }

    public InvalidStatusException(ExceptionEnums enums) {
        super(enums);
    }
}
