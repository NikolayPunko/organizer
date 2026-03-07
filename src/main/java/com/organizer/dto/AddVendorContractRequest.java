package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddVendorContractRequest {
    private Long weddingId;
    private Long serviceId;
    private BigDecimal price;
}