package com.genAI.microservice.paymentgateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PaymentGatewayApplication.class);
		app.setAdditionalProfiles("dev"); // Activate 'dev' profile
		app.run(args);
	}

}