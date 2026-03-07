package com.organizer.repository;

import com.organizer.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByWeddingId(Long weddingId);

    List<Payment> findByContractId(Long contractId);

    List<Payment> findByPaymentType(String paymentType);

    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

}