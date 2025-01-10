package com.genAI.microservice.paymentgateway_service.events;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.service.AccountingService;
import com.genAI.microservice.paymentgateway_service.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventListener.class);

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private ReportingService reportingService;

    private PaymentDetails paymentDetails;

    @KafkaListener(topics = "transaction-completed-topic", groupId = "payment-gateway")
    public void listenPaymentEvent(PaymentEvent event, Acknowledgment acknowledgment) {
        logger.info("Received payment event:{}", event);
        try {
            // Handle the transaction completed event
            accountingService.updateAccountingRecords(event);
            logger.info("Transaction Completed for event:", event);

            // Process the event, e.g., record the payment in the accounting system
            accountingService.recordPayment(paymentDetails);
            acknowledgment.acknowledge();

            // Generate a summary report of payment methods used.
            reportingService.generatePaymentMethodUsageReport();

        } catch (Exception e) {
            // Log and handle the exception
            logger.error("Error processing the event", e);
            throw new RuntimeException("Failed to process the event", e);
        }
    }

    @DltHandler
    public void dltListen(Object in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Received at DLT: " + in + " from topic: " + topic);
    }
}
