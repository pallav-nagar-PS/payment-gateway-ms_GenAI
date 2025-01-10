package com.genAI.microservice.paymentgateway_service.steps;

import com.genAI.microservice.paymentgateway_service.PaymentGatewayApplication;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.TransactionStatus;
import com.genAI.microservice.paymentgateway_service.service.PaymentProcessor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {PaymentGatewayApplication.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentProcessingSteps {

    @Autowired
    private PaymentProcessor paymentProcessor;

    private PaymentDetails paymentDetails;
    private CompletableFuture<TransactionStatus> future;
    private TransactionStatus transactionStatus;

    @Given("a valid payment request")
    public void a_valid_payment_request() {
        paymentDetails = new PaymentDetails(BigDecimal.valueOf(100.00),"EUR", "USD", paymentDetails.getPaymentMethods(), "John Doe");
    }

    @When("the payment is processed")
    public void the_payment_is_processed() {
        future = paymentProcessor.processPayment(paymentDetails);
        try {
            transactionStatus = future.get();
        } catch (Exception e) {
            CompletableFuture<TransactionStatus> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
        }
    }

    @Then("the response should indicate a successful transaction")
    public void the_response_should_indicate_a_successful_transaction() {
        assertEquals("Success", transactionStatus.getStatus());
    }
}

