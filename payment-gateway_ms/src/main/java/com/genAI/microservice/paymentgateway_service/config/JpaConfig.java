package com.genAI.microservice.paymentgateway_service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan("com.genAI.microservice.paymentgateway_service.jpa_entity")
public class JpaConfig {
}
