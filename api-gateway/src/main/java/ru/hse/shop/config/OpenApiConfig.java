package ru.hse.shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI CustomOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Api Gateway API")
                .version("1.0")
                .description("service for routing requests"));
    }
}
