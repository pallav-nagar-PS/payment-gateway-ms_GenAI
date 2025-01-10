package com.genAI.microservice.paymentgateway_service.controller;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.jpa_entity.PaymentTransaction;
import com.genAI.microservice.paymentgateway_service.model.*;
import com.genAI.microservice.paymentgateway_service.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping("/api/payment")
public class PaymentGatewayController {

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    private PaymentProcessor paymentProcessor;

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private ReportingService reportingService;


    @PostMapping("/processPayment")
    @Operation(summary = "Process a payment",
            description = "Processes a payment and records the transaction details.",
            responses = {
                    @ApiResponse(description = "Payment processed successfully", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TransactionStatus.class))),
                    @ApiResponse(description = "Payment processing failed", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<CompletableFuture<TransactionStatus>> processPayment(@RequestBody PaymentDetails paymentDetails) {
        CompletableFuture<TransactionStatus> status = paymentProcessor.processPayment(paymentDetails);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/publishPaymentEvent")
    @Operation(summary = "Publish Payment Event to Kafka",
            description = "Publishes a payment event message to a Kafka topic to be consumed by the PaymentEventListener.",
            responses = {
                    @ApiResponse(description = "Event published successfully", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "Failed to publish event", responseCode = "500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public String publishPaymentEvent(@RequestBody PaymentEvent event) {
        kafkaTemplate.send("transaction-completed-topic", event);
        return "Event published";
    }

    @PostMapping("/recordPayment")
    @Operation(summary = "Record a payment transaction in the accounting system.",
            description = "Indicates if the transaction was recorded successfully.",
            responses = {
                    @ApiResponse(description = "Payment recorded successfully", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "Payment processing failed", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<String> recordPayment(@RequestBody PaymentDetails paymentDetails) {
        boolean success = accountingService.recordPayment(paymentDetails);
        return success ? ResponseEntity.ok("Payment recorded successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to record payment");
    }

    @PostMapping("/refundPayment")
    @Operation(summary = "Process a refund",
            description = "Processes a refund for a previous transaction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Refund processed successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Refund processing failed",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> processRefund(@RequestBody RefundDetails refundDetails) {
        // Implementation of refund processing
        boolean success = accountingService.recordRefund(refundDetails);
        return success ? ResponseEntity.ok("Refund recorded successfully") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to record Refund");
    }

    @GetMapping("/reportTransactions")
    @Operation(summary = "Report all transactions.",
            description = "Generates a report of all transactions within a specified date range.",
            responses = {
                    @ApiResponse(description = "Report generated successfully", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentTransaction.class))),
                    @ApiResponse(description = "Report generation failed", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<List<PaymentTransaction>> getTransactionReports(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<PaymentTransaction> report = reportingService.generateTransactionReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reportPaymentMethods")
    @Operation(summary = "Payment method usage report",
            description = "Generates a summary report of payment methods used.",
            responses = {
                    @ApiResponse(description = "Report generated successfully", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentMethodUsageReport.class))),
                    @ApiResponse(description = "Report generation failed", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<PaymentMethodUsageReport> generatePaymentMethodUsageReport() {
        // Implementation of payment method usage report
        PaymentMethodUsageReport report = reportingService.generatePaymentMethodUsageReport();
        return ResponseEntity.ok(report);
    }
}
