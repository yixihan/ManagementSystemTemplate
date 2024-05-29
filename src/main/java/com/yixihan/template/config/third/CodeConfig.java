package com.yixihan.template.config.third;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码 配置
 *
 * @author yixihan
 * @date 2024-05-29 09:58
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "third.code")
public class CodeConfig implements InitializingBean {

    private Integer len;

    private Long timeOut;

    private String commonKey;

    @Override
    public void afterPropertiesSet() {
        log.info("code len: {}", len);
        log.info("code timeOut: {} min", timeOut);
        log.info("code redis commonKey: {}", commonKey);
    }
}
