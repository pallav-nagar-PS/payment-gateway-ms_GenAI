package com.genAI.microservice.paymentgateway_service.config;

import com.genAI.microservice.paymentgateway_service.service.CurrencyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CurrencyConverter currencyConverter(RestTemplate restTemplate, @Value("${currency.api.baseurl}") String apiBaseUrl, @Value("${currency.api.key}") String apiKey) {
        return new CurrencyConverter(restTemplate, apiBaseUrl, apiKey);
    }
}