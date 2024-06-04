package com.yixihan.template.controller;

import com.yixihan.template.vo.resp.base.ApiResp;
import lombok.extern.slf4j.Slf4j;

import java.util.function.*;

/**
 * 基础 Controller
 *
 * @author yixihan
 * @date 2024-05-30 09:39
 */
@Slf4j
public class BaseController {

    public static ApiResp<Void> run(Runnable function) {
        function.run();
        return ApiResp.succ();
    }

    public static <T> ApiResp<Void> run(Consumer<T> function, T params) {
        function.accept(params);
        return ApiResp.succ();
    }

    public static <T, U> ApiResp<Void> run(BiConsumer<T, U> function, T paramOne, U paramTwo) {
        function.accept(paramOne, paramTwo);
        return ApiResp.succ();
    }

    public static <R> ApiResp<R> run(Supplier<R> function) {
        return ApiResp.succ(function.get());
    }

    public static <T, R> ApiResp<R> run(Function<T, R> function, T params) {
        return ApiResp.succ(function.apply(params));
    }

    public static <T, U, R> ApiResp<R> run(BiFunction<T, U, R> function, T paramOne, U paramTwo) {
        return ApiResp.succ(function.apply(paramOne, paramTwo));
    }
}
