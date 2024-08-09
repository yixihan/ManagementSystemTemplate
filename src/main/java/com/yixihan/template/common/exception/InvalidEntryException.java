package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 无效 id 异常
 *
 * @author yixihan
 * @date 2024-05-27 09:48
 */
@SuppressWarnings("unused")
public class InvalidEntryException extends BizException {

    @Serial
    private static final long serialVersionUID = 2917508877577564465L;

    public InvalidEntryException() {
        super(ExceptionEnums.INVALID_CODE_ERR);
    }

    public InvalidEntryException(String message) {
        super(message);
    }

    public InvalidEntryException(Throwable e) {
        super(e);
    }

    public InvalidEntryException(ExceptionEnums enums) {
        super(enums);
    }
}
