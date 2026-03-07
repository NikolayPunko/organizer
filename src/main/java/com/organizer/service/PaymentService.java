package com.organizer.service;

import com.organizer.entity.Payment;
import com.organizer.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> findByWeddingId(Long weddingId) {
        return paymentRepository.findByWeddingId(weddingId);
    }

    public List<Payment> findByContractId(Long contractId) {
        return paymentRepository.findByContractId(contractId);
    }

    public List<Payment> findByPaymentType(String paymentType) {
        return paymentRepository.findByPaymentType(paymentType);
    }

    public List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment updatedPayment) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(updatedPayment.getAmount());
        payment.setPaymentType(updatedPayment.getPaymentType());
        payment.setPaymentDate(updatedPayment.getPaymentDate());
        payment.setNotes(updatedPayment.getNotes());
        payment.setWedding(updatedPayment.getWedding());
        payment.setContract(updatedPayment.getContract());

        return paymentRepository.save(payment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}