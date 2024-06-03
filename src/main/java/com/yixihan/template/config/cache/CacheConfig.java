package com.yixihan.template.config.cache;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 缓存参数 配置
 *
 * @author yixihan
 * @date 2024-05-30 15:33
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfig implements InitializingBean {

    private Long timeout;

    private List<CacheName> cacheNames;

    @Override
    public void afterPropertiesSet() {
        cacheNames.forEach(it -> it.setTimeout(ObjUtil.isNull(it.getTimeout()) ? timeout : it.getTimeout()));
        log.info("default cache timeout: {} min", timeout);
        log.info("cache name list: {}", cacheNames);
    }


    @Data
    static class CacheName {
        private String name;

        private Long timeout;

        @Override
        public String toString() {
            return StrUtil.format("[cacheName: {}, timeout: {} min]", name, timeout);
        }
    }


}
