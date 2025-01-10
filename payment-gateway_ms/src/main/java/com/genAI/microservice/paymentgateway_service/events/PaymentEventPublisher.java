package com.genAI.microservice.paymentgateway_service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventPublisher {
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void publishTransactionCompleted(PaymentEvent paymentEvent) {
        kafkaTemplate.send("transaction-completed-topic", paymentEvent);
    }

}
