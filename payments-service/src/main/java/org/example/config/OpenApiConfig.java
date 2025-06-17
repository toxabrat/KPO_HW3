package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI CustomOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Payments Service API")
                .version("1.0")
                .description("service for storing account information"));
    }
}
