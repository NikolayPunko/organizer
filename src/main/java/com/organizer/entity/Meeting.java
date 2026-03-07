package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;

    private String location;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;

}