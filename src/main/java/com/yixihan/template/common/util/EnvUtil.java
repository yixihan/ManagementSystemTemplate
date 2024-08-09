package com.yixihan.template.common.util;

import cn.hutool.extra.spring.SpringUtil;

/**
 * 环境工具类
 *
 * @author yixihan
 * @date 2024-05-26 11:37
 */
@SuppressWarnings("unused")
public class EnvUtil {

    private EnvUtil() {
    }

    private static final String DEV_ENV = "dev";
    private static final String TEST_ENV = "test";
    private static final String UAT_ENV = "uat";
    private static final String PROD_ENV = "prod";

    /**
     * 判断当前环境是否为 dev 环境
     */
    public static boolean dev() {
        return DEV_ENV.equals(getEnvName());
    }

    /**
     * 判断当前环境是否为 test 环境
     */
    public static boolean test() {
        return TEST_ENV.equals(getEnvName());
    }

    /**
     * 判断当前环境是否为 uat 环境
     */
    public static boolean uat() {
        return UAT_ENV.equals(getEnvName());
    }

    /**
     * 判断当前环境是否为 prod 环境
     */
    public static boolean prod() {
        return PROD_ENV.equals(getEnvName());
    }

    /**
     * 获取当前环境名
     */
    public static String getEnvName() {
        return SpringUtil.getActiveProfile();
    }
}
