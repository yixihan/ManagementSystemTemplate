package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 参数校验异常
 *
 * @author yixihan
 * @date 2024-05-21 15:07
 */
@SuppressWarnings("unused")
public class InvalidParameterException extends BizException {

    @Serial
    private static final long serialVersionUID = 3182914630424422322L;


    public InvalidParameterException() {
        super(ExceptionEnums.INVALID_REQ_ERR);
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(Throwable e) {
        super(e);
    }

    public InvalidParameterException(ExceptionEnums enums) {
        super(enums);
    }
}
