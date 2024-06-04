package com.yixihan.template.config.third;

import com.yixihan.template.enums.OsTypeEnums;
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

    private OsTypeEnums type;

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String bucketName;

    private String host;

    private String token;

    private String username;

    private String password;


    @Override
    public void afterPropertiesSet() {
        log.info("os type: {}", type.getDesc());
        log.info("os accessKey: {}", accessKey);
        log.info("os secretKey: {}", secretKey);
        log.info("os endpoint: {}", endpoint);
        log.info("os bucketName: {}", bucketName);
        log.info("os host: {}", host);

        if (smms()) {
            log.info("SM.MS username: {}", username);
            log.info("SM.MS password: {}", password);
            log.info("SM.MS token: {}", token);
        }
    }

    /**
     * 是否为本地存储
     */
    public Boolean local() {
        return OsTypeEnums.LOCAL.equals(type);
    }

    /**
     * 是否为数据库存储
     */
    public Boolean db() {
        return OsTypeEnums.DB.equals(type);
    }

    /**
     * 是否为阿里云 oss 存储
     */
    public Boolean oss() {
        return OsTypeEnums.OSS.equals(type);
    }

    /**
     * 是否为腾讯云 cos 存储
     */
    public Boolean cos() {
        return OsTypeEnums.COS.equals(type);
    }

    /**
     * 是否为七牛云 Kodo 存储
     */
    public Boolean kodo() {
        return OsTypeEnums.KODO.equals(type);
    }

    /**
     * 是否为 SM.MS 存储
     */
    public Boolean smms() {
        return OsTypeEnums.SMMS.equals(type);
    }
}
