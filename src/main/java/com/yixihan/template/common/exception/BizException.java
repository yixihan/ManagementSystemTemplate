package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 业务异常, 所有业务自定义异常均继承此类
 *
 * @author yixihan
 * @date 2024-05-21 15:02
 */
@SuppressWarnings("unused")
public class BizException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1659049298300474679L;

    public BizException() {
        this(ExceptionEnums.FAILED_TYPE_BUSINESS);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable e) {
        super(e);
    }

    public BizException(ExceptionEnums enums) {
        super(enums);
    }
}
