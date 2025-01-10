package com.genAI.microservice.paymentgateway_service;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.events.PaymentEventListener;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import com.genAI.microservice.paymentgateway_service.service.AccountingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.GenericMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class PaymentEventListenerTests {

    @Mock
    private AccountingService accountingService;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private PaymentEventListener paymentEventListener;

    @Mock
    private PaymentDetails paymentDetails;

    @Mock
    private TransactionStatus status;

    @Test
    public void testPaymentEventHandling() {
        PaymentEvent paymentEvent = new PaymentEvent("123", status, paymentDetails);
        GenericMessage<PaymentEvent> message = new GenericMessage<>(paymentEvent);

        paymentEventListener.listenPaymentEvent(paymentEvent, acknowledgment);

        verify(accountingService, times(1)).recordPayment(any(PaymentDetails.class));
        verify(acknowledgment, times(1)).acknowledge();
    }
}
