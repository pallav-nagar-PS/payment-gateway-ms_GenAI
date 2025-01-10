package com.genAI.microservice.paymentgateway_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Payment Gateway Microservice API")
                .version("v1")
                        .description("This API handles payment processing and management for the Payment Gateway Service"));
    }

   /*@Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("Payment Gateway Service")
                .pathsToMatch("/api/**")
                .build();
    }*/
}

