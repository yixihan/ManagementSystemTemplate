package com.yixihan.template.service.third;

import com.yixihan.template.enums.CodeTypeEnums;
import com.yixihan.template.vo.req.third.CodeValidateReq;
import com.yixihan.template.vo.req.third.EmailSendReq;
import com.yixihan.template.vo.req.third.SmsSendReq;

/**
 * 验证码 服务
 *
 * @author yixihan
 * @date 2024-05-29 09:52
 */
public interface CodeService {

    /**
     * 验证码生成
     *
     * @param keyName redis 缓存key
     * @return 验证码
     */
    String getCode(String keyName);

    /**
     * 验证码校验
     *
     * @param keyName redis 缓存 key
     * @param code    验证码
     */
    void validate(String keyName, String code);

    /**
     * 将验证码存入 redis 中， 并设置有效时间
     *
     * @param keyName redis key
     * @param code    验证码
     */
    void addRedis(String keyName, String code);

    /**
     * 邮件发送
     *
     * @param dtoReq 请求参数
     */
    String sendEmail(EmailSendReq dtoReq);

    /**
     * 邮件验证码验证
     *
     * @param dtoReq 请求参数
     */
    void validateEmail(CodeValidateReq dtoReq);

    /**
     * 发送短信
     *
     * @param req 请求参数
     */
    String sendSms(SmsSendReq req);

    /**
     * 校验短信验证码
     * <p>
     * 短信发送类型枚举类 : {@link CodeTypeEnums}
     *
     * @param req 请求参数
     */
    void validateSms(CodeValidateReq req);
}
