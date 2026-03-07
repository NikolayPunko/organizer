package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name1;

    private String name2;

    private String phone;

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}