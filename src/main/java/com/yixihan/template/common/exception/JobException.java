package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 任务 异常
 *
 * @author yixihan
 * @date 2024-05-27 09:44
 */
@SuppressWarnings("unused")
public class JobException extends BizException {

    @Serial
    private static final long serialVersionUID = 8260812108237979421L;

    public JobException() {
        super(ExceptionEnums.JOB_ERR);
    }

    public JobException(String message) {
        super(message);
    }

    public JobException(Throwable e) {
        super(e);
    }

    public JobException(ExceptionEnums enums) {
        super(enums);
    }
}
