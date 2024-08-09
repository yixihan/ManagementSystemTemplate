package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;
import lombok.Getter;

import java.io.Serial;

/**
 * base 异常
 *
 * @author yixihan
 * @date 2024-05-21 14:54
 */
@Getter
@SuppressWarnings("unused")
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6850331125274641714L;

    protected final Integer code;

    protected final String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

    public BaseException(ExceptionEnums enums) {
        super(enums.getMessage());
        this.message = enums.getMessage();
        this.code = enums.getCode();
    }

    public BaseException(Throwable e) {
        super(e.getMessage(), e);
        this.message = e.getMessage();
        this.code = 500;
    }
}
