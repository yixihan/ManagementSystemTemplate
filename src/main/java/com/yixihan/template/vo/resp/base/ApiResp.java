package com.yixihan.template.vo.resp.base;

import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.BaseException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求返回体
 *
 * @author yixihan
 * @date 2024-05-21 18:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
@Schema(description = "请求返回体")
public class ApiResp<T> {

    private static final Integer SUCCESS_CODE = 200;

    @Schema(description = "返回状态码")
    private Integer code;

    @Schema(description = "返回信息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    public ApiResp(Integer code) {
        this.code = code;
    }

    public ApiResp(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResp(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // ====================== comm resp ======================
    public static <T> ApiResp<T> resp(Integer code, String message, T data) {
        return new ApiResp<>(code, message, data);
    }

    public static <T> ApiResp<T> resp(Integer code, T data) {
        return new ApiResp<>(code, data);
    }

    public static <T> ApiResp<T> resp(Integer code, String message) {
        return new ApiResp<>(code, message);
    }

    // ====================== success resp
    public static <T> ApiResp<T> succ() {
        return succ(null, null);
    }

    public static <T> ApiResp<T> succ(Void empty) {
        return succ(null, null);
    }

    public static <T> ApiResp<T> succ(T data) {
        return succ(null, data);
    }

    public static <T> ApiResp<T> succ(String message) {
        return succ(message, null);
    }

    public static <T> ApiResp<T> succ(String message, T data) {
        return resp(SUCCESS_CODE, message, data);
    }

    // ====================== fail resp without exception ======================
    public static <T> ApiResp<T> error(String message) {
        return error(ExceptionEnums.SYSTEM_EXCEPTION.getCode(), message, null);
    }

    public static <T> ApiResp<T> error(Integer code, String message) {
        return error(code, message, null);
    }

    public static <T> ApiResp<T> error(Integer code, String message, T data) {
        return resp(code, message, data);
    }

    // ====================== fail resp with exception ======================
    public static <T> ApiResp<T> error(BaseException e) {
        return error(e, null);
    }

    public static <T> ApiResp<T> error(BaseException e, T data) {
        return error(e.getCode(), e.getMessage(), data);
    }

    public static <T> ApiResp<T> error(Throwable e) {
        return error(ExceptionEnums.SYSTEM_EXCEPTION.getCode(), e.getMessage(), null);
    }

    public static <T> ApiResp<T> error(Throwable e, T data) {
        return error(ExceptionEnums.SYSTEM_EXCEPTION.getCode(), e.getMessage(), data);
    }
}
