package com.organizer.controller;

import com.organizer.entity.Payment;
import com.organizer.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.findAll();
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return paymentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @GetMapping("/wedding/{weddingId}")
    public List<Payment> getByWeddingId(@PathVariable Long weddingId) {
        return paymentService.findByWeddingId(weddingId);
    }

    @GetMapping("/contract/{contractId}")
    public List<Payment> getByContractId(@PathVariable Long contractId) {
        return paymentService.findByContractId(contractId);
    }

    @GetMapping("/type/{paymentType}")
    public List<Payment> getByPaymentType(@PathVariable String paymentType) {
        return paymentService.findByPaymentType(paymentType);
    }

    @GetMapping("/range")
    public List<Payment> getByDateRange(@RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) {
        return paymentService.findByDateRange(startDate, endDate);
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return paymentService.save(payment);
    }

    @PutMapping("/{id}")
    public Payment update(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.update(id, payment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        paymentService.delete(id);
    }
}