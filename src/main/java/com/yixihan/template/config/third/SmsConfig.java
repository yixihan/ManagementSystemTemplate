package com.yixihan.template.config.third;

import com.yixihan.template.common.enums.SmsSourceEnums;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SMS 配置
 *
 * @author yixihan
 * @date 2024-05-29 09:58
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "third.sms")
public class SmsConfig implements InitializingBean {

    private Boolean mock;

    private SmsSourceEnums source;

    private String secretId;

    private String secretKey;

    private String smsSdkAppId;

    private String signName;

    // **** redis key **** //

    private String loginKey;

    private String registerKey;

    private String updatePasswordKey;

    private String commonKey;

    @Override
    public void afterPropertiesSet() {
        log.info("sms mock open: {}", mock);
        log.info("sms source: {}", source.getDesc());
        log.info("sms secretId: {}", secretId);
        log.info("sms secretKey: {}", secretKey);
        log.info("sms smsSdkAppId: {}", smsSdkAppId);
        log.info("sms signName: {}", signName);
        log.info("sms login redis ket: {}", loginKey);
        log.info("sms register redis ket: {}", registerKey);
        log.info("sms update password redis ket: {}", updatePasswordKey);
        log.info("sms common redis ket: {}", commonKey);
    }
}
