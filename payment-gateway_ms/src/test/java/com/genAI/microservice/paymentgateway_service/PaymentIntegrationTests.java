package com.genAI.microservice.paymentgateway_service;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PaymentIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private PaymentDetails paymentDetails;

    @Test
    public void testPaymentEndpoint() {

        paymentDetails = new PaymentDetails(BigDecimal.valueOf(100.00), "USD", "EUR", paymentDetails.getPaymentMethods(), "John Doe");
        ResponseEntity<TransactionStatus> response = testRestTemplate.postForEntity("/api/payments/processPayment", paymentDetails, TransactionStatus.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
    }
}
