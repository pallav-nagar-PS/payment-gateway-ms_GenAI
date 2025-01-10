package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.events.PaymentEvent;
import com.genAI.microservice.paymentgateway_service.events.PaymentEventListener;
import com.genAI.microservice.paymentgateway_service.jpa_entity.PaymentTransaction;
import com.genAI.microservice.paymentgateway_service.model.PaymentDetails;
import com.genAI.microservice.paymentgateway_service.model.PaymentMethod;
import com.genAI.microservice.paymentgateway_service.model.PaymentMethodUsageReport;
import com.genAI.microservice.paymentgateway_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("dev")
public class ReportingService {

    private PaymentRepository paymentRepository;

    private PaymentDetails paymentDetails;

    /**
     * Generates a report of all transactions within a specified date range.
     * @param startDate The start date of the period for the report.
     * @param endDate The end date of the period for the report.
     * @return A list of transactions that occurred within the date range.
     */
    public List<PaymentTransaction> generateTransactionReport(Date startDate, Date endDate) {
        return paymentRepository.findAllByTransactionDateBetween(startDate, endDate);
    }

    /**
     * Generates a summary report of payment methods used.
     * @return A report detailing the usage of different payment methods.
     */
    public PaymentMethodUsageReport generatePaymentMethodUsageReport() {
        // Logic to generate the payment method usage report
        List<PaymentMethod> paymentMethodList = paymentDetails.getPaymentMethods();
        Map<String, Integer> usageCounts = new HashMap<>();
        for(PaymentMethod paymentMethod : paymentMethodList){
                List<PaymentTransaction> transactions = paymentRepository.findAllByPaymentMethod(paymentMethod.getPaymentMethod());
                usageCounts.put(paymentMethod.getPaymentMethod(), transactions.size());
        }

        return new PaymentMethodUsageReport(usageCounts);
    }
}
