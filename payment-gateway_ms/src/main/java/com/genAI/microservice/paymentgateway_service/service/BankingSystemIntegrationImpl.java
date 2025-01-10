package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionResponse;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class BankingSystemIntegrationImpl implements BankingSystemIntegrationService {

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    public BankingSystemIntegrationImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param paymentDetails
     * @return
     */
    @Override
    public TransactionStatus processBankPayment(PaymentDetails paymentDetails) {

        // Here, we would have logic to interact with the bank's API
        // For example, we might construct an HTTP request to the bank's payment processing endpoint
        String bankEndpoint = "https://api.bank.com/processPayment";
        TransactionResponse response = restTemplate.postForObject(bankEndpoint, paymentDetails, TransactionResponse.class);

        if (response.isSuccess()) {
            return new TransactionStatus("Success", "Bank transaction completed successfully");
        } else {
            return new TransactionStatus("Failed", response.getErrorMessage());
        }
    }

}
