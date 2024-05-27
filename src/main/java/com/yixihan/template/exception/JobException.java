package com.yixihan.template.exception;

import java.io.Serial;

/**
 * 任务 异常
 *
 * @author yixihan
 * @date 2024-05-27 09:44
 */
public class JobException extends BizException {

    @Serial
    private static final long serialVersionUID = 8260812108237979421L;

    public JobException(String message) {
        super(message);
    }

}
