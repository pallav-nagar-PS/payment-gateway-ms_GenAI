package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.config.AsyncConfig;
import com.genAI.microservice.paymentgateway_service.events.PaymentEventPublisher;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.PaymentMethod;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class PaymentProcessor {
    private final PaymentEventPublisher eventPublisher;
    private final BankingSystemIntegrationImpl bankingService;
    private final ThirdPartyProviderIntegrationImpl thirdPartyProviderService;
    private final CurrencyConverter currencyConverter;

    @Autowired
    private PaymentEvent paymentEvent;

    @Autowired
    @Qualifier(value = "taskExecutor")
    private final AsyncConfig asyncConfig;

    @Autowired
    private static TransactionStatus status;

    @Autowired
    public PaymentProcessor(PaymentEventPublisher eventPublisher, BankingSystemIntegrationImpl bankingService, ThirdPartyProviderIntegrationImpl thirdPartyPaymentService, CurrencyConverter currencyConverter, AsyncConfig asyncConfig) {
        this.eventPublisher = eventPublisher;
        this.bankingService = bankingService;
        this.thirdPartyProviderService = thirdPartyPaymentService;
        this.currencyConverter = currencyConverter;
        this.asyncConfig = asyncConfig;
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<TransactionStatus> processPayment(PaymentDetails paymentDetails) {
        asyncConfig.taskExecutor().initialize();
        try {
            // Convert currency if source and target currencies are different
            if (!paymentDetails.getFromCurrency().equals(paymentDetails.getToCurrency())) {
                BigDecimal convertedAmount = currencyConverter.convert(paymentDetails.getFromCurrency(), paymentDetails.getToCurrency(), paymentDetails.getAmount());
                paymentDetails.setAmount(convertedAmount);
            }

            // Process payment based on the payment method
            List<PaymentMethod> paymentMethodList = paymentDetails.getPaymentMethods();

            for(PaymentMethod paymentMethod : paymentMethodList) {
                switch (paymentMethod.getPaymentMethod()) {
                    case "bank_transfer":
                        status = bankingService.processBankPayment(paymentDetails);
                        break;
                    case "credit_card":
                    case "debit_card":
                    case "paypal":
                    case "bitcoin":
                        status = thirdPartyProviderService.processThirdPartyPayment(paymentDetails);
                        break;
                    default:
                        return CompletableFuture.completedFuture(new TransactionStatus("Failed", "Unsupported payment method"));
                }

                // Emit event if transaction is successful
                if ("Success".equals(status.getStatus())) {
                    paymentEvent = new PaymentEvent((paymentEvent.getTransactionId()), paymentEvent.getStatus(), paymentDetails);
                    eventPublisher.publishTransactionCompleted(paymentEvent);
                }
            }
            return CompletableFuture.completedFuture(status);
        } catch (Exception e) {
            CompletableFuture<TransactionStatus> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
}


