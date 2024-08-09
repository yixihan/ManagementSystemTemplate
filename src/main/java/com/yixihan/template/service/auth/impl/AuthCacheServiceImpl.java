package com.yixihan.template.service.auth.impl;

import cn.hutool.json.JSONUtil;
import com.yixihan.template.common.constant.AuthConstant;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.service.auth.AuthCacheService;
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
public class AuthCacheServiceImpl implements AuthCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String token, AuthVO auth) {
        redisTemplate.opsForHash().put(AuthConstant.AUTH_LOGIN_KEY, token, JSONUtil.toJsonStr(auth));
    }

    @Override
    public AuthVO get(String token) {
        Assert.isTrue(contains(token), ExceptionEnums.TOKEN_ERR);

        Object jsonStr = redisTemplate.opsForHash().get(AuthConstant.AUTH_LOGIN_KEY, token);

        Assert.notNull(jsonStr, ExceptionEnums.TOKEN_ERR);

        return JSONUtil.toBean(jsonStr.toString(), AuthVO.class);
    }

    @Override
    public Boolean contains(String token) {
        return redisTemplate.opsForHash().get(AuthConstant.AUTH_LOGIN_KEY, token) != null;
    }

    @Override
    public void del(String token) {
        redisTemplate.opsForHash().delete(AuthConstant.AUTH_LOGIN_KEY, token);
    }
}
