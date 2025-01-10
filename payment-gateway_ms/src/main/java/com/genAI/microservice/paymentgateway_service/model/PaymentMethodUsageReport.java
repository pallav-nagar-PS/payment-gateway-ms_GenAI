package com.genAI.microservice.paymentgateway_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class PaymentMethodUsageReport {
    private Map<String, Integer> usageCounts;
}
