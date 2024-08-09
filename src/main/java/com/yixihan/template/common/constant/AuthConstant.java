package com.yixihan.template.common.constant;

/**
 * 权限认证 常量
 *
 * @author yixihan
 * @date 2024-05-23 11:19
 */
@SuppressWarnings("unused")
public class AuthConstant {

    private AuthConstant() {
    }

    public static final String JWT_TOKEN = "token";

    public static final String AUTH_LOGIN_KEY = "authCache:Login";

    public static final String AUTH_LOGOUT_KEY = "authCache:Logout:{}";

    public static final String USER_ID = "userId";

    public static final String USER_NAME = "userName";


}
