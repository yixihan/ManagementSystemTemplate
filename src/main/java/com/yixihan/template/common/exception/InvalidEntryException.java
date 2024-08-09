package com.yixihan.template.common.exception;

import java.io.Serial;

/**
 * 无效 id 异常
 *
 * @author yixihan
 * @date 2024-05-27 09:48
 */
public class InvalidEntryException extends BizException {

    @Serial
    private static final long serialVersionUID = 2917508877577564465L;

    public InvalidEntryException(String message) {
        super(message);
    }
}
