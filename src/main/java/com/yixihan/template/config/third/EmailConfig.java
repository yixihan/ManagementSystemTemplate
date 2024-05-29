package com.yixihan.template.config.third;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件 配置
 *
 * @author yixihan
 * @date 2024-05-29 09:58
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "third.email")
public class EmailConfig implements InitializingBean {

    private Boolean mock;

    private String sendEmail;

    private String title;


    // **** redis key **** //

    private String loginKey;

    private String registerKey;

    private String updatePasswordKey;

    private String commonKey;

    @Override
    public void afterPropertiesSet() {
        log.info("email mock open: {}", mock);
        log.info("email send email: {}", sendEmail);
        log.info("email default title: {}", title);
        log.info("email login redis ket: {}", loginKey);
        log.info("email register redis ket: {}", registerKey);
        log.info("email update password redis ket: {}", updatePasswordKey);
        log.info("email common redis ket: {}", commonKey);
    }
}
