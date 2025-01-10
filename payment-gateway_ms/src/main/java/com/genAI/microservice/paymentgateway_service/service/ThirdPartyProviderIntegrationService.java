package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;

public interface ThirdPartyProviderIntegrationService {
    TransactionStatus processThirdPartyPayment(PaymentDetails paymentDetails);
}
