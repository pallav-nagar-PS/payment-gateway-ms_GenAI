package com.genAI.microservice.paymentgateway_service;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.events.PaymentEventListener;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"transaction-completed-topic"})
public class KafkaIntegrationTests {

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    private PaymentDetails paymentDetails;

    private TransactionStatus status;

    @Test
    public void testEventPublishingAndConsuming() {
        PaymentEvent event = new PaymentEvent("123", status, paymentDetails);
        kafkaTemplate.send("transaction-completed-topic", event);

        // Additional synchronization/wait might be needed to ensure the message is consumed
        // Assertions to verify the event was consumed and processed correctly
    }
}
