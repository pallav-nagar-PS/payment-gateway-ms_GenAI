package com.genAI.microservice.paymentgateway_service.events;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@EnableKafka
@Transactional
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;// JSON string containing all relevant payment details
    private TransactionStatus status;
    private PaymentDetails paymentDetails;

    public PaymentEvent(PaymentDetails paymentDetails, TransactionStatus status) {
        this.paymentDetails = paymentDetails;
        this.status = status;
    }
}
