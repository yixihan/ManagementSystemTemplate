package com.yixihan.template.config.cache;

import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理 配置
 *
 * @author yixihan
 * @date 2024-05-30 15:44
 */
@Configuration
public class CacheManageConfig {

    @Resource
    private CacheConfig cacheConfig;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
        for (CacheConfig.CacheName cacheName : cacheConfig.getCacheNames()) {
            RedisCacheConfiguration timeout = RedisCacheConfiguration
                    .defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(cacheName.getTimeout()));
            configurations.put(cacheName.getName(), timeout);
        }
        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(configurations)
                .build();
    }
}
