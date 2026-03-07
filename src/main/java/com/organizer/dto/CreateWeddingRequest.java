package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateWeddingRequest {
    private Long clientId;
    private LocalDate weddingDate;
    private BigDecimal budget;
    private Long userId;
}