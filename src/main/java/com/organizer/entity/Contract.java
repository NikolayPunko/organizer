package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter
@Setter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreed_price")
    private BigDecimal agreedPrice;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

}