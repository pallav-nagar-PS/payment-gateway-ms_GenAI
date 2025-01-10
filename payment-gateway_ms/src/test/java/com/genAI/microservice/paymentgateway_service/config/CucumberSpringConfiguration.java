package com.genAI.microservice.paymentgateway_service.config;

import com.genAI.microservice.paymentgateway_service.PaymentGatewayApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = {PaymentGatewayApplication.class})
@ContextConfiguration
public class CucumberSpringConfiguration {
}
