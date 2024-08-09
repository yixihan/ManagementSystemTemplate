package com.yixihan.template.config.third;

import com.yixihan.template.common.enums.OsTypeEnums;
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

    private String region;

    private String host;

    private String token;

    private String username;

    private String password;

    private String localPath;


    @Override
    public void afterPropertiesSet() {
        log.info("os type: {}", type.getDesc());
        if (smms()) {
            log.info("SM.MS username: {}", username);
            log.info("SM.MS password: {}", password);
            log.info("SM.MS token: {}", token);
        } else if (local()) {
            log.info("Local file dir: {}", localPath);
        } else if (kodo()) {
            log.info("Kodo accessKey: {}", accessKey);
            log.info("Kodo secretKey: {}", secretKey);
            log.info("Kodo host: {}", host);
            log.info("Kodo bucketName: {}", bucketName);
        } else if (oss()) {
            log.info("OSS accessKey: {}", accessKey);
            log.info("OSS secretKey: {}", secretKey);
            log.info("OSS endpoint: {}", endpoint);
            log.info("OSS host: {}", host);
            log.info("OSS bucketName: {}", bucketName);
        } else if (cos()) {
            log.info("COS accessKey: {}", accessKey);
            log.info("COS secretKey: {}", secretKey);
            log.info("COS region: {}", region);
            log.info("COS host: {}", host);
            log.info("COS bucketName: {}", bucketName);
        } else if (db()) {
            // do nothing
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
