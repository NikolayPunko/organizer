package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weddings")
@Getter
@Setter
public class Wedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wedding_date")
    private LocalDate weddingDate;

    private String status;

    @Column(name = "budget_total")
    private BigDecimal budgetTotal;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}