package com.yixihan.template.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc Swagger 配置
 *
 * @author yixihan
 * @date 2024-05-21 14:12
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI springDocOpenAPI(Info info) {
        return new OpenAPI()
                .info(info)
                .externalDocs(new ExternalDocumentation()
                        .description("摸鱼小屋")
                        .url("https://yixihan.chat"));
    }

    @Bean
    public Info springDocInfo() {
        License license = new License()
                .name("License")
                .url("https://yixihan.chat");
        Contact contact = new Contact()
                .name("yixihan")
                .email("wangben850115@gmail.com");

        return new Info()
                .title("管理系统模板 OpenAPI")
                .description("管理系统模板 OpenAPI")
                .version("v1.0.0")
                .license(license)
                .contact(contact);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
