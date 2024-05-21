package com.yixihan.template.handler;

import com.yixihan.template.exception.BaseException;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.vo.resp.base.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * controller 异常捕获器
 *
 * @author yixihan
 * @date 2024-05-21 18:33
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 项目异常捕获
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ApiResp<Object> handleSystemException(BaseException e) {
        log.error("出现异常", e);
        return ApiResp.error(e.getMessage());
    }

    /**
     * 通用业务异常捕获
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ApiResp<Object> handleBizException(BizException e) {
        log.error("业务异常", e);
        return ApiResp.error(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public ApiResp<Object> handleNullPointerException(NullPointerException e) {
        log.error("出现异常", e);
        return ApiResp.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public ApiResp<Object> handleRuntimeException(RuntimeException e) {
        log.error("出现异常", e);
        return ApiResp.error(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResp<Object> handleException(Exception e) {
        log.error("出现异常", e);
        return ApiResp.error(e.getMessage());
    }
}
