package com.yixihan.template.common.util;

import cn.hutool.core.util.StrUtil;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.exception.*;

/**
 * 异常工具
 *
 * @author yixihan
 * @date 2024-05-26 11:44
 */
public class Panic {

    public static void noAuth(String errMsg) {
        throw new AuthException(errMsg);
    }

    public static void noAuth(ExceptionEnums enums) {
        throw new AuthException(enums);
    }

    public static void noSuchJob(String errMsg) {
        throw new JobException(errMsg);
    }

    public static void noSuchEntry(Long id) {
        throw new InvalidEntryException(StrUtil.format("can not find Entry[Id : {}]", id));
    }

    public static void invalidStatus(String mark) {
        throw new InvalidStatusException(StrUtil.format("Entry[{}] status is invalid", mark));
    }

    public static void invalidParam(String param) {
        throw new InvalidParameterException(StrUtil.format("req param[{}] is invalid", param));
    }
}