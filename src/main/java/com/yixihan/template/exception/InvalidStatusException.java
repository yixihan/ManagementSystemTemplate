package com.yixihan.template.exception;

import java.io.Serial;

/**
 * 状态校验异常
 *
 * @author yixihan
 * @date 2024-05-27 09:56
 */
public class InvalidStatusException extends BizException{

    @Serial
    private static final long serialVersionUID = -3020266562817335135L;

    public InvalidStatusException(String message) {
        super(message);
    }
}
