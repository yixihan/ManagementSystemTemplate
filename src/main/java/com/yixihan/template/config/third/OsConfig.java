package com.yixihan.template.config.third;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OS 配置
 *
 * @author yixihan
 * @date 2024-05-29 09:58
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "third.os")
public class OsConfig implements InitializingBean {

    private String type;

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String bucketName;

    private String host;


    @Override
    public void afterPropertiesSet() {
        log.info("os type: {}", type);
        log.info("os accessKey: {}", accessKey);
        log.info("os secretKey: {}", secretKey);
        log.info("os endpoint: {}", endpoint);
        log.info("os bucketName: {}", bucketName);
        log.info("os host: {}", host);
    }
}
