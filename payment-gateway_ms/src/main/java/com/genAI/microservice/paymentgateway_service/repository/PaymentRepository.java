package com.genAI.microservice.paymentgateway_service.repository;

import com.genAI.microservice.paymentgateway_service.jpa_entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PaymentTransaction p WHERE p.id = :id")
    PaymentTransaction findByIdForUpdate(Long id);

    List<PaymentTransaction> findAllByTransactionDateBetween(Date startDate, Date endDate);

    List<PaymentTransaction> findAllByPaymentMethod(String paymentMethod);
}
