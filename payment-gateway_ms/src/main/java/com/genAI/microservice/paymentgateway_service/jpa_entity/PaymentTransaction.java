package com.genAI.microservice.paymentgateway_service.jpa_entity;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.PaymentMethod;
import com.genAI.microservice.paymentgateway_service.model.RefundDetails;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Transactional
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private PaymentDetails paymentDetails;

    @JdbcTypeCode(SqlTypes.JSON)
    private RefundDetails refundDetails;

    private PaymentEvent paymentEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    private PaymentMethod paymentMethod;

    public PaymentTransaction(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public PaymentTransaction(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }

    public PaymentTransaction(RefundDetails refundDetails) {
        this.refundDetails = refundDetails;
    }

    public PaymentTransaction(PaymentMethod paymentMethod){
        this.paymentMethod = paymentMethod;
    }
}

