package com.yixihan.template.exception;

import com.yixihan.template.enums.ExceptionEnums;

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


    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException() {
        super(ExceptionEnums.FAILED_TYPE_BUSINESS);
    }

    public InvalidParameterException(Throwable e) {
        super(ExceptionEnums.PARAMS_VALID_ERR, e);
    }
}
