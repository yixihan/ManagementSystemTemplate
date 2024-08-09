package com.yixihan.template.common.exception;

import com.yixihan.template.common.enums.ExceptionEnums;

import java.io.Serial;

/**
 * 图片验证码 异常
 *
 * @author yixihan
 * @date 2024-05-29 09:49
 */
@SuppressWarnings("unused")
public class PictureException extends BizException {

    @Serial
    private static final long serialVersionUID = -8022684844350624592L;

    public PictureException() {
        super(ExceptionEnums.PICTURE_CODE_ERR);
    }

    public PictureException(String message) {
        super(message);
    }

    public PictureException(Throwable e) {
        super(e);
    }

    public PictureException(ExceptionEnums enums) {
        super(enums);
    }
}
