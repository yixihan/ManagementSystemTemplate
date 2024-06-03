package com.yixihan.template.util.builder;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.yixihan.template.config.third.SmsConfig;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.enums.SmsSourceEnums;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送构建类
 *
 * @author yixihan
 * @date 2024-05-29 10:44
 */
@Slf4j
public class SmsBuilder {

    private String toMobile;

    private String templateId;

    private Map<String, String> params;

    private String source;

    private SmsBuilder() {

    }

    public static SmsBuilder build() {
        return new SmsBuilder();
    }

    public SmsBuilder toMobile(String mobile) {
        this.toMobile = mobile;
        return this;
    }

    public SmsBuilder templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public SmsBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public SmsBuilder addParams(String key, String value) {
        if (ObjUtil.isNull(params)) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public SmsBuilder source(SmsSourceEnums source) {
        this.source = source.getValue();
        return this;
    }

    public void send() {
        // params validate
        Assert.notBlank(toMobile);
        Assert.notBlank(templateId);
        Assert.notBlank(source);


        if (SmsSourceEnums.TENCENT.getValue().equals(source)) {
            tencentSend();
        } else if (SmsSourceEnums.AL.getValue().equals(source)) {
            alSend();
        } else {
            // todo
        }
    }

    private void alSend() {
        SmsConfig smsConfig = SpringUtil.getBean(SmsConfig.class);
        try {
            com.aliyun.dysmsapi20170525.Client client = createClient(smsConfig);
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                    // 发送手机号
                    .setPhoneNumbers(toMobile)
                    // 短信签名
                    .setSignName(smsConfig.getSignName())
                    // 模板 code
                    .setTemplateCode(templateId)
                    // 模板参数
                    .setTemplateParam(JSONUtil.toJsonStr(params));
            com.aliyun.dysmsapi20170525.models.SendSmsResponse sendResp = client.sendSmsWithOptions(sendSmsRequest, new com.aliyun.teautil.models.RuntimeOptions());
            String code = sendResp.body.code;
            if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
                log.error("短信服务出现异常: {}", sendResp.body.message);
                throw new BizException(ExceptionEnums.SMS_SEND_ERR);
            }
        } catch (Exception e) {
            log.error("短信服务出现异常!", e);
            throw new BizException(ExceptionEnums.SMS_SEND_ERR);
        }
    }

    public static com.aliyun.dysmsapi20170525.Client createClient(SmsConfig smsConfig) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(smsConfig.getSecretId())
                .setAccessKeySecret(smsConfig.getSecretKey());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public void tencentSend() {
        try {
            SmsConfig smsConfig = SpringUtil.getBean(SmsConfig.class);
            SmsClient client = getSmsClient(smsConfig);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {toMobile};
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSmsSdkAppId(smsConfig.getSmsSdkAppId());
            req.setSignName(smsConfig.getSignName());
            req.setTemplateId(templateId);
            String[] templateParamSet1 = params.values().toArray(new String[0]);
            req.setTemplateParamSet(templateParamSet1);
            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            SendStatus sendStatus = resp.getSendStatusSet()[0];

            Assert.isTrue("Ok".equals(sendStatus.getCode()), ExceptionEnums.SMS_SEND_ERR);
        } catch (TencentCloudSDKException e) {
            log.error("短信服务出现异常!", e);
            throw new BizException(ExceptionEnums.SMS_SEND_ERR);
        }
    }

    private static SmsClient getSmsClient(SmsConfig smsConfig) {
        Credential cred = new Credential(smsConfig.getSecretId(), smsConfig.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        return new SmsClient(cred, "ap-guangzhou", clientProfile);
    }
}
