package com.genAI.microservice.paymentgateway_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TransactionStatus {
    private String status;
    private String message;
}
