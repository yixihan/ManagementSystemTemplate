package com.yixihan.template.auth.cache;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yixihan.template.auth.constant.AuthConstant;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.JwtUtil;
import com.yixihan.template.vo.resp.user.AuthVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 权限缓存服务
 *
 * @author yixihan
 * @date 2024-05-23 14:33
 */
@Slf4j
@Service
public class AuthCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存放用户登录信息
     *
     * @param token jwtToken
     * @param auth  登录信息
     */
    public void put(String token, AuthVO auth) {
        redisTemplate.opsForHash().put(AuthConstant.AUTH_LOGIN_KEY, token, JSONUtil.toJsonStr(auth));
    }

    /**
     * 取出用户登录信息
     *
     * @param token jwtToken
     * @return 登录信息
     */
    public AuthVO get(String token) {
        Assert.isTrue(contains(token), ExceptionEnums.TOKEN_ERR);

        Object jsonStr = redisTemplate.opsForHash().get(AuthConstant.AUTH_LOGIN_KEY, token);

        Assert.notNull(jsonStr, ExceptionEnums.TOKEN_ERR);

        return JSONUtil.toBean(jsonStr.toString(), AuthVO.class);
    }

    /**
     * 判断缓存中是否有用户信息
     *
     * @param token jwtToken
     * @return 有 : true | 无 : false
     */
    public Boolean contains(String token) {
        return redisTemplate.opsForHash().get(AuthConstant.AUTH_LOGIN_KEY, token) != null;
    }

    /**
     * 删除用户登录信息
     *
     * @param token jwtToken
     */
    public void del(String token) {
        redisTemplate.opsForHash().delete(AuthConstant.AUTH_LOGIN_KEY, token);
    }

    /**
     * 存放已失效 token
     *
     * @param token jwtToken
     */
    public void putLogoutToken(String token) {
        // 存入 redis
        String key = StrUtil.format(AuthConstant.AUTH_LOGOUT_KEY, token);
        redisTemplate.opsForValue().set(key, token);

        // 设置过期时间
        redisTemplate.expire(key, JwtUtil.EXPIRE_TIME, TimeUnit.DAYS);
    }

    /**
     * 判断缓存中是否有已失效 token
     *
     * @param token jwtToken
     * @return 有 : true | 无 : false
     */
    public Boolean containsLogoutToken(String token) {
        String key = StrUtil.format(AuthConstant.AUTH_LOGOUT_KEY, token);
        Long expire = redisTemplate.getExpire(key);

        // 校验验证码是否已经过期
        if (expire != null && expire > 0) {
            return true;
        }

        return redisTemplate.opsForValue().get(key) != null;
    }

}
