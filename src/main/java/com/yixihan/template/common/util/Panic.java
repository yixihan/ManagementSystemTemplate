package com.yixihan.template.common.util;

import cn.hutool.core.util.StrUtil;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.exception.*;
import com.yixihan.template.model.BaseModel;

/**
 * 异常抛出工具
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

    public static void noSuchJob() {
        throw new JobException(ExceptionEnums.NO_SUCH_JOB_ERR);
    }

    public static void noSuchEntry(BaseModel model) {
        throw new InvalidEntryException(StrUtil.format("can not find {}[PrimaryKey : {}]", model.getClass().getSimpleName(), model.getPK()));
    }

    public static void noSuchEntry(Long pk) {
        throw new InvalidEntryException(StrUtil.format("can not find Entry[PrimaryKey : {}]", pk));
    }

    public static void invalidStatus(String mark) {
        throw new InvalidStatusException(StrUtil.format("Entry[{}] status is invalid", mark));
    }

    public static void invalidParam(String param) {
        throw new InvalidParameterException(StrUtil.format("req param[{}] is invalid", param));
    }
}
