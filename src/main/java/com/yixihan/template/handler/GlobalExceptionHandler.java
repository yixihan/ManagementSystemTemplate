package com.yixihan.template.handler;

import cn.hutool.core.util.StrUtil;
import com.yixihan.template.exception.BaseException;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.util.EnvUtil;
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
        logError(e);
        return ApiResp.error(e.getMessage());
    }

    /**
     * 通用业务异常捕获
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ApiResp<Object> handleBizException(BizException e) {
        logError(e);
        return ApiResp.error(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public ApiResp<Object> handleNullPointerException(NullPointerException e) {
        logError(e);
        return ApiResp.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public ApiResp<Object> handleRuntimeException(RuntimeException e) {
        logError(e);
        return ApiResp.error(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResp<Object> handleException(Exception e) {
        logError(e);
        return ApiResp.error(e.getMessage());
    }

    private static void logError(Throwable e) {
        if (EnvUtil.dev()) {
            log.error(StrUtil.format("出现异常, {}", e.getMessage()), e);
        } else {
            log.error("出现异常, {}", e.getMessage());
        }
    }
}
