package com.yixihan.template.service.third.impl;

import com.yixihan.template.config.third.CodeConfig;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.CodeException;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.util.Assert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码 服务
 *
 * @author yixihan
 * @date 2024-05-29 09:52
 */
@Slf4j
@Service
public class CodeServiceImpl implements CodeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CodeConfig codeConfig;

    private final Random random = new Random();

    private static final char[] RANDOM_ARR = "1234567890".toCharArray();

    public String getCode(String keyName) {
        // 生成验证码
        String code = getRandomCode();

        // 存入 redis
        addRedis(keyName, code);
        return code;
    }

    @Override
    public void validate(String keyName, String code) {
        Long expire = stringRedisTemplate.getExpire(keyName);

        // 校验验证码是否已经过期
        Assert.isFalse(expire == null || expire < 0L, new CodeException(ExceptionEnums.CODE_EXPIRE_ERR));

        // 校验验证码是否正确
        Assert.isTrue(code.equals(stringRedisTemplate.opsForValue().get(keyName)),
                new CodeException(ExceptionEnums.CODE_VALIDATE_ERROR));
    }

    /**
     * 获取随机验证码, 并存入 redis 中
     *
     * @return code
     */
    private synchronized String getRandomCode() {
        int len = codeConfig.getLen();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(RANDOM_ARR[random.nextInt(RANDOM_ARR.length)]);
        }

        return sb.toString();
    }

    @Override
    public void addRedis(String keyName, String code) {
        // 存入 redis
        stringRedisTemplate.opsForValue().set(keyName, code);

        // 设置过期时间
        stringRedisTemplate.expire(keyName, codeConfig.getTimeOut(), TimeUnit.MINUTES);
    }
}
