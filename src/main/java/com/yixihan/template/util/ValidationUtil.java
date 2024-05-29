package com.yixihan.template.util;

import cn.hutool.core.util.StrUtil;

/**
 * 正则校验工具类
 *
 * @author yixihan
 * @date 2024-05-29 14:05
 */
@SuppressWarnings("unused")
public class ValidationUtil {

    /**
     * 数字
     */
    private static final String REG_NUMBER = ".*\\d+.*";

    /**
     * 小写字母
     */
    private static final String REG_UPPERCASE = ".*[A-Z]+.*";

    /**
     * 大写字母
     */
    private static final String REG_LOWERCASE = ".*[a-z]+.*";

    /**
     * 特殊符号
     */
    private static final String REG_SYMBOL = ".*[-。，￥！·=；：‘“、~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    /**
     * 邮箱
     */
    private static final String EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * 手机号
     */
    private static final String MOBILE = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6-7])|(17[1-8])|(18[0-9])|(19[1|3])|(19[5|6])|(19[8|9]))\\d{8}$";

    /**
     * 身份证
     */
    private static final String ID_CARD = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|([Xx]))";

    /**
     * 密码最小长度
     */
    private static final Integer PASSWORD_MIN_LENGTH = 8;

    /**
     * 密码最大长度
     */
    private static final Integer PASSWORD_MAX_LENGTH = 20;

    /**
     * 用户名最小长度
     */
    private static final Integer USERNAME_MIN_LENGTH = 2;

    /**
     * 用户名最大长度
     */
    private static final Integer USERNAME_MAX_LENGTH = 10;

    private ValidationUtil() {
    }

    /**
     * 校验邮箱
     *
     * @param email 邮箱
     */
    public static Boolean validateEmail(String email) {
        return StrUtil.isNotBlank(email) && email.matches(EMAIL);
    }

    /**
     * 校验手机号
     *
     * @param mobile 手机号
     */
    public static Boolean validateMobile(String mobile) {
        return StrUtil.isNotBlank(mobile) && mobile.matches(MOBILE);
    }

    /**
     * 校验身份证号
     *
     * @param idCard 身份证号
     */
    public static Boolean validateIdCard(String idCard) {
        return StrUtil.isNotBlank(idCard) && idCard.matches(ID_CARD);
    }

    /**
     * 校验用户名
     *
     * @param userName 用户名
     */
    public static Boolean validateUserName(String userName) {
        return !StrUtil.isBlank(userName)
                && userName.length() >= USERNAME_MIN_LENGTH
                && userName.length() <= USERNAME_MAX_LENGTH;
    }

    /**
     * 校验密码, 除长度外, 其余规则, 四种有两个即可
     * <p>
     * <li>密码长度 : [8~20]</li>
     * <li>有大写字母</li>
     * <li>有小写字母</li>
     * <li>有数字</li>
     * <li>有特殊符号</li>
     * </p>
     *
     * @param password 密码
     */
    public static Boolean validatePassword(String password) {
        if (StrUtil.isBlank(password)
                || password.length() < PASSWORD_MIN_LENGTH
                || password.length() > PASSWORD_MAX_LENGTH) {
            return Boolean.FALSE;
        }
        int i = password.matches(REG_NUMBER) ? 1 : 0;
        i += password.matches(REG_LOWERCASE) ? 1 : 0;
        i += password.matches(REG_UPPERCASE) ? 1 : 0;
        i += password.matches(REG_SYMBOL) ? 1 : 0;

        return i >= 2;
    }
}
