package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

}