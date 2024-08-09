package com.yixihan.template.common.util;

import cn.hutool.core.util.StrUtil;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.exception.BaseException;
import com.yixihan.template.common.exception.BizException;

/**
 * 断言工具
 *
 * @author yixihan
 * @date 2024-05-23 14:34
 */
@SuppressWarnings("unused")
public class Assert extends org.springframework.util.Assert {

    private Assert() {
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
     *
     * @param expression 布尔值
     * @param enums      指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void isTrue(Boolean expression, ExceptionEnums enums) {
        isTrue(expression, new BizException(enums));
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
     *
     * @param expression 布尔值
     * @param e          指定断言不通过时抛出的异常
     */
    public static void isTrue(Boolean expression, BizException e) {
        if (Boolean.FALSE.equals(expression)) {
            throw e;
        }
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出{@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param expression 布尔值
     */
    public static void isTrue(Boolean expression) {
        isTrue(expression, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出给定的异常<br>
     *
     * @param expression 布尔值
     * @param enums      指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void isFalse(Boolean expression, ExceptionEnums enums) {
        isFalse(expression, new BizException(enums));
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出给定的异常<br>
     *
     * @param expression 布尔值
     * @param e          指定断言不通过时抛出的异常
     */
    public static void isFalse(Boolean expression, BizException e) {
        if (Boolean.TRUE.equals(expression)) {
            throw e;
        }
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出{@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param expression 布尔值
     */
    public static void isFalse(Boolean expression) {
        isFalse(expression, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言对象是否不为 {@code  null}，如果为 {@code null} 抛出给定的异常<br>
     *
     * @param obj   对象
     * @param enums 指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void notNull(Object obj, ExceptionEnums enums) {
        notNull(obj, new BizException(enums));
    }

    /**
     * 断言对象是否不为 {@code  null}，如果为 {@code null} 抛出给定的异常<br>
     *
     * @param obj 对象
     * @param e   指定断言不通过时抛出的异常
     */
    public static void notNull(Object obj, BizException e) {
        if (obj == null) {
            throw e;
        }
    }

    /**
     * 断言对象是否不为 {@code  null}，如果为 {@code null} 抛出 {@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param obj 对象
     */
    public static void notNull(Object obj) {
        notNull(obj, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言对象是否为 {@code  null}，如果不为 {@code null} 抛出给定的异常<br>
     *
     * @param obj 对象
     * @param e   指定断言不通过时抛出的异常
     */
    public static void isNull(Object obj, BizException e) {
        if (obj == null) {
            throw e;
        }
    }

    /**
     * 断言对象是否为 {@code  null}，如果不为 {@code null} 抛出给定的异常<br>
     *
     * @param obj   对象
     * @param enums 指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void isNull(Object obj, ExceptionEnums enums) {
        isNull(obj, new BizException(enums));
    }

    /**
     * 断言对象是否为 {@code  null}，如果不为 {@code null} 抛出 {@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param obj 对象
     */
    public static void isNull(Object obj) {
        isNull(obj, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言字符串是否不为空，如果为 {@code null} 抛出给定的异常<br>
     *
     * @param str   字符串
     * @param enums 指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void notBlank(String str, ExceptionEnums enums) {
        notBlank(str, new BizException(enums));
    }

    /**
     * 断言字符串是否不为 {@code  Blank}，如果为 {@code Blank} 抛出给定的异常<br>
     *
     * @param str 字符串
     * @param e   指定断言不通过时抛出的异常
     */
    public static void notBlank(String str, BizException e) {
        if (StrUtil.isBlank(str)) {
            throw e;
        }
    }

    /**
     * 断言字符串是否不为 {@code  Blank}，如果为 {@code Blank} 抛出 {@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param str 字符串
     */
    public static void notBlank(String str) {
        notBlank(str, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言字符串是否为 {@code  Blank}，如果不为 {@code Blank} 抛出给定的异常<br>
     *
     * @param str 字符串
     * @param e   指定断言不通过时抛出的异常
     */
    public static void isBlank(String str, BizException e) {
        if (StrUtil.isNotBlank(str)) {
            throw e;
        }
    }

    /**
     * 断言字符串是否为 {@code  Blank}，如果不为 {@code Blank} 抛出给定的异常<br>
     *
     * @param str   字符串
     * @param enums 指定断言不通过时抛出的异常信息 {@link ExceptionEnums}
     */
    public static void isBlank(String str, ExceptionEnums enums) {
        isBlank(str, new BizException(enums));
    }

    /**
     * 断言字符串是否为 {@code  Blank}，如果不为 {@code Blank} 抛出 {@code ExceptionEnums.PARAMS_VALID_ERR}<br>
     *
     * @param str 字符串
     */
    public static void isBlank(String str) {
        isBlank(str, ExceptionEnums.PARAMS_VALID_ERR);
    }

    /**
     * 断言 enumName 是枚举值
     * 要使用, enumName 必须是枚举的 name
     *
     * @param enumName  枚举值
     * @param enumClass 枚举类
     */
    public static <E extends Enum<E>> void isEnum(String enumName, Class<E> enumClass) {
        if (!enumClass.isEnum()) {
            throw new BaseException(StrUtil.format("class[{}] is not enum class", enumClass));
        }
        notBlank(enumName);

        boolean flag = false;
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(enumName)) {
                flag = true;
                break;
            }
        }
        isTrue(flag);
    }
}