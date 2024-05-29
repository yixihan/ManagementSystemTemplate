package com.yixihan.template.service.third.impl;

import cn.hutool.core.util.RandomUtil;
import com.yixihan.template.config.third.CodeConfig;
import com.yixihan.template.config.third.EmailConfig;
import com.yixihan.template.config.third.SmsConfig;
import com.yixihan.template.enums.CodeTypeEnums;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.AuthException;
import com.yixihan.template.exception.CodeException;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.third.TemplateService;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.builder.EmailBuilder;
import com.yixihan.template.util.builder.SmsBuilder;
import com.yixihan.template.vo.req.third.CodeValidateReq;
import com.yixihan.template.vo.req.third.EmailSendReq;
import com.yixihan.template.vo.req.third.SmsSendReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
    private UserService userService;

    @Resource
    private TemplateService templateService;

    @Resource
    private CodeConfig codeConfig;

    @Resource
    private EmailConfig emailConfig;

    @Resource
    private SmsConfig smsConfig;

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
        return RandomUtil.randomNumbers(codeConfig.getLen());
    }

    @Override
    public void addRedis(String keyName, String code) {
        // 存入 redis
        stringRedisTemplate.opsForValue().set(keyName, code);

        // 设置过期时间
        stringRedisTemplate.expire(keyName, codeConfig.getTimeOut(), TimeUnit.MINUTES);
    }

    @Override
    public String sendEmail(EmailSendReq req) {
        CodeTypeEnums codeType = CodeTypeEnums.valueOf(req.getType());
        String keyName = getEmailRedisKey(req.getEmail(), codeType);
        String code = getCode(keyName);

        if (emailConfig.getMock()) {
            return code;
        }

        String emailContent = templateService.getTemplateContent(codeType.getType());

        EmailBuilder.build()
                .toEmail(req.getEmail())
                .fromEmail(emailConfig.getSendEmail())
                .subject(emailConfig.getTitle())
                .content(emailContent, code, codeConfig.getTimeOut())
                .send();

        return null;
    }

    @Override
    public void validateEmail(CodeValidateReq req) {
        // 生成 keyName
        CodeTypeEnums codeType = CodeTypeEnums.valueOf(req.getType());
        String keyName = getEmailRedisKey(req.getEmail(), codeType);
        validate(keyName, req.getCode());
    }

    /**
     * 获取 email redis key
     *
     * @param email    email
     * @param codeType 邮箱类型 {@link CodeTypeEnums}
     * @return redis key
     */
    private String getEmailRedisKey(String email, CodeTypeEnums codeType) {
        String key;
        switch (codeType) {
            case LOGIN:
                key = String.format(emailConfig.getLoginKey(), email);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserEmail(email), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            case REGISTER:
                key = String.format(emailConfig.getRegisterKey(), email);
                break;
            case PASSWORD:
                key = String.format(emailConfig.getUpdatePasswordKey(), email);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserEmail(email), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            case COMMON:
                key = String.format(emailConfig.getCommonKey(), email);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserEmail(email), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            default:
                throw new AuthException(ExceptionEnums.PARAMS_VALID_ERR);
        }

        return key;
    }

    @Override
    public String sendSms(SmsSendReq req) {
        CodeTypeEnums codeType = CodeTypeEnums.valueOf(req.getType());
        String keyName = getSmsRedisKey(req.getMobile(), codeType);
        String code = getCode(keyName);

        if (smsConfig.getMock()) {
            return code;
        }

        String templateId = templateService.getTemplateContent(codeType.getType());

        SmsBuilder.build()
                .toMobile(req.getMobile())
                .templateId(templateId)
                .source(smsConfig.getSource())
                .addParams("code", code)
                .addParams("timeOut", String.valueOf(codeConfig.getTimeOut()))
                .send();

        return null;
    }

    @Override
    public void validateSms(CodeValidateReq req) {
        // 生成 keyName
        CodeTypeEnums codeType = CodeTypeEnums.valueOf(req.getType());
        String keyName = getSmsRedisKey(req.getMobile(), codeType);
        validate(keyName, req.getCode());
    }

    /**
     * 获取 sms redis key
     *
     * @param mobile   mobile
     * @param codeType 邮箱类型 {@link CodeTypeEnums}
     * @return redis key
     */
    private String getSmsRedisKey(String mobile, CodeTypeEnums codeType) {
        String key;
        switch (codeType) {
            case LOGIN:
                key = String.format(emailConfig.getLoginKey(), mobile);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserMobile(mobile), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            case REGISTER:
                key = String.format(emailConfig.getRegisterKey(), mobile);
                break;
            case PASSWORD:
                key = String.format(emailConfig.getUpdatePasswordKey(), mobile);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserMobile(mobile), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            case COMMON:
                key = String.format(emailConfig.getCommonKey(), mobile);
                // 非注册类型, 校验用户是否存在
                Assert.isTrue(userService.validateUserMobile(mobile), ExceptionEnums.ACCOUNT_NOT_FOUND);
                break;
            default:
                throw new AuthException(ExceptionEnums.PARAMS_VALID_ERR);
        }

        return key;
    }
}
