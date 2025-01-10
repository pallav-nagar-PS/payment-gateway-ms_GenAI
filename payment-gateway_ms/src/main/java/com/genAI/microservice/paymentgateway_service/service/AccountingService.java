package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.jpa_entity.PaymentTransaction;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.RefundDetails;
import com.genAI.microservice.paymentgateway_service.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AccountingService {
    private static final Logger logger = LoggerFactory.getLogger(AccountingService.class);

    private PaymentRepository paymentRepository;

    @KafkaListener(topics = "transaction-completed-topic", groupId = "payment-gateway")
    public void updateAccountingRecords(PaymentEvent event) {
        // Logic to update transaction records based on event details
        PaymentTransaction transaction = new PaymentTransaction(event);
        paymentRepository.save(transaction);
        logger.info("Handling payment event: ", event);
    }

    /**
     * Records a payment transaction in the accounting system.
     * @param paymentDetails Details of the payment transaction.
     * @return A boolean indicating if the transaction was recorded successfully.
     */
    public boolean recordPayment(PaymentDetails paymentDetails) {
        // Logic to update accounting records
        PaymentTransaction transaction = new PaymentTransaction(paymentDetails);
        paymentRepository.findByIdForUpdate(transaction.getId());
        paymentRepository.save(transaction);
        logger.info("Recording payment: ", paymentDetails);
        return true;
    }

    /**
     * Records a refund transaction in the accounting system.
     * @param refundDetails Details of the refund transaction.
     * @return A boolean indicating if the transaction was recorded successfully.
     */
    public boolean recordRefund(RefundDetails refundDetails) {
        PaymentTransaction refundTransaction = new PaymentTransaction(refundDetails);
        paymentRepository.findByIdForUpdate(refundTransaction.getId());
        paymentRepository.save(refundTransaction);
        logger.info("Recording refund: ", refundDetails);
        return true;
    }
}
