package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterPaymentRequest {
    private Long weddingId;
    private Long contractId;
    private BigDecimal amount;
    private String paymentType;
}