package com.yixihan.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Application
 */
@Slf4j
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication
public class BootStrap {

    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
    }

}
