package com.yixihan.template.auth.cache;

import cn.hutool.json.JSONUtil;
import com.yixihan.template.auth.constant.AuthConstant;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.util.Assert;
import com.yixihan.template.vo.resp.user.AuthVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
        redisTemplate.opsForHash().put(AuthConstant.AUTH_KEY, token, JSONUtil.toJsonStr(auth));
    }

    /**
     * 取出用户登录信息
     *
     * @param token jwtToken
     * @return 登录信息
     */
    public AuthVO get(String token) {
        Assert.isTrue(contains(token), ExceptionEnums.TOKEN_ERR);

        Object jsonStr = redisTemplate.opsForHash().get(AuthConstant.AUTH_KEY, token);

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
        return redisTemplate.opsForHash().get(AuthConstant.AUTH_KEY, token) != null;
    }

    /**
     * 删除用户登录信息
     *
     * @param token jwtToken
     */
    public void del(String token) {
        redisTemplate.opsForHash().delete(AuthConstant.AUTH_KEY, token);
    }

}
