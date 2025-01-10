package com.genAI.microservice.paymentgateway_service;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import com.genAI.microservice.paymentgateway_service.service.CurrencyConverter;
import com.genAI.microservice.paymentgateway_service.service.PaymentProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PaymentProcessorTests {

    @Mock
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    @Mock
    private CurrencyConverter currencyConverter;

    private PaymentDetails paymentDetails;

    @Test
    public void testPaymentProcessingWithCurrencyConversion() throws Exception {
        paymentDetails = new PaymentDetails(BigDecimal.valueOf(100.00), "USD", "EUR", paymentDetails.getPaymentMethods(), "John Doe");
        when(currencyConverter.convert("USD", "EUR", BigDecimal.valueOf(100.00))).thenReturn(BigDecimal.valueOf(85.0));

        CompletableFuture<TransactionStatus> future = paymentProcessor.processPayment(paymentDetails);
        TransactionStatus status = future.get();  // Blocks until the result is available

        assertEquals("EUR", paymentDetails.getToCurrency());
        assertEquals(BigDecimal.valueOf(85.0), paymentDetails.getAmount());
        verify(currencyConverter, times(1)).convert("USD", "EUR", BigDecimal.valueOf(100.00));
        assertEquals("Success", status.getStatus());
    }

}
