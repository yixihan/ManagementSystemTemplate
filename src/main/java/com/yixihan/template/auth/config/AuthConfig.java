package com.yixihan.template.auth.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限认证配置
 *
 * @author yixihan
 * @date 2024-05-23 11:21
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class AuthConfig implements InitializingBean {

    private String controllerPackage;

    private List<String> ignoreUrlList;

    @Override
    public void afterPropertiesSet() {
        log.info("controllerPackage: {}", controllerPackage);
        log.info("ignoreUrlList: {}", ignoreUrlList);
    }
}