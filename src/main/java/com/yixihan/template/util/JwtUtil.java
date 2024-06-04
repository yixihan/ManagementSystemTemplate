package com.yixihan.template.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.AuthException;

import java.util.Date;
import java.util.Map;

/**
 * jwt 工具
 *
 * @author yixihan
 * @date 2024-05-21 15:14
 */
@SuppressWarnings("unused")
public class JwtUtil {
    /**
     * JwtToken 的过期时间, 30 天
     */
    public static final int EXPIRE_TIME = 30;

    private JwtUtil() {

    }


    /**
     * 生成 JwtToken
     * <li> JwtToken 内存储的信息为 用户 id, 用户名, 为 String 类型的数据</li>
     * <li>传入的签名为用户加密后的密码</li>
     *
     * @param secret  用户加密后的密码
     * @param payload 有效载荷
     * @return JwtToken
     */
    public static String createJwtToken(String secret, Map<String, Object> payload) {
        // 获取当前时间
        Date nowTime = DateUtil.date();

        // 设置过期时间
        Date expireTime = DateUtil.offsetDay(nowTime, EXPIRE_TIME);

        // 创建 jwt
        JWT jwt = JWT.create();

        // 设置有效负荷
        payload.forEach(jwt::setPayload);

        return jwt
                // 设置签发时间
                .setIssuedAt(nowTime)
                // 设置过期时间
                .setExpiresAt(expireTime)
                // 设置签名
                .setKey(StrUtil.bytes(secret))
                .sign();
    }

    /**
     * 获取 JwtToken 有效载荷内容
     *
     * @param token       JwtToken
     * @param payloadName 载荷名
     * @param targetClass 载荷内容类型
     * @return 载荷内容
     */
    public static <T> T analysis(String token, String payloadName, Class<T> targetClass) {
        try {
            // 获取 JWT
            JWT jwt = JWT.of(token);

            return Convert.convert(targetClass, jwt.getPayload().getClaim(payloadName));
        } catch (Exception e) {
            throw new AuthException(ExceptionEnums.TOKEN_ERR);
        }
    }

    /**
     * 校验 JwtToken
     *
     * @return true : 校验通过 | false : 校验失败
     */
    public static Boolean validateToken(String token, String secret) {
        return JWTUtil.verify(token, StrUtil.bytes(secret));
    }

    /**
     * 校验 JwtToken 时间, 共校验三种时间
     * <p>
     * <li>签发时间</li>
     * <li>生效时间</li>
     * <li>过期时间</li>
     * </p>
     *
     * @return true : 校验通过 | false : 校验失败
     */
    public static Boolean validateDate(String token) {
        try {
            JWTValidator.of(token).validateDate(DateUtil.date());
        } catch (ValidateException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
