package com.genAI.microservice.paymentgateway_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Transactional
public class RefundDetails {
    private String transactionId;
    private double amount;
    private String currency;
    private String reason;
}
