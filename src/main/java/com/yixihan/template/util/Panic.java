package com.yixihan.template.util;

import com.yixihan.template.exception.AuthException;

/**
 * description
 *
 * @author yixihan
 * @date 2024-05-26 11:44
 */
public class Panic {

    public static void noAuth(String errMsg) {
        throw new AuthException(errMsg);
    }
}
