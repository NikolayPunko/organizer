package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private TeamMember assignedTo;

}