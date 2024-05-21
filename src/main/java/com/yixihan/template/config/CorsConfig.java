package com.yixihan.template.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域 配置
 *
 * @author yixihan
 * @date 2024-05-21 11:59
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对所有路径启用CORS
        registry.addMapping("/**")
                // 允许所有来源访问
                .allowedOriginPatterns("*")
                // 允许所有HTTP方法
                .allowedMethods("*")
                // 允许所有头
                .allowedHeaders("*")
                // 允许证书(cookies)
                .allowCredentials(true)
                // 设置预检请求的缓存时间(秒)
                .maxAge(3600);
    }
}