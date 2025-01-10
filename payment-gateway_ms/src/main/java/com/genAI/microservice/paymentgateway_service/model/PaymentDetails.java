package com.genAI.microservice.paymentgateway_service.model;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Transactional
public class PaymentDetails {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private List<PaymentMethod> paymentMethods;
    private String userDetails;
}
