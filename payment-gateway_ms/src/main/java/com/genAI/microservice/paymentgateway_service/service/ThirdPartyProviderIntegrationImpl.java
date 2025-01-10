package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.PaymentMethod;
import com.genAI.microservice.paymentgateway_service.model.TransactionResponse;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ThirdPartyProviderIntegrationImpl implements ThirdPartyProviderIntegrationService {

    @Autowired
    private final RestTemplate restTemplate;

    private TransactionStatus status;

    @Autowired
    public ThirdPartyProviderIntegrationImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param paymentDetails
     * @return
     */
    @Override
    public TransactionStatus processThirdPartyPayment(PaymentDetails paymentDetails) {
        // Interaction with a third-party payment provider's API
        // For instance, sending a payment request to Stripe or PayPal
        String providerEndpoint = "https://api.paymentprovider.com/processPayment";
        TransactionResponse response = restTemplate.postForObject(providerEndpoint, paymentDetails, TransactionResponse.class);

        List<PaymentMethod> paymentMethods = paymentDetails.getPaymentMethods();
        for(PaymentMethod paymentMethod : paymentMethods) {
            if (response.isSuccess())
                status = new TransactionStatus("Success", "Payment processed successfully via: " + paymentMethod.getPaymentMethod());
            else
                status = new TransactionStatus("Failed", response.getErrorMessage());
        }

        return status;
    }
}
