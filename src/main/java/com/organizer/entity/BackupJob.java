package com.organizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_jobs")
@Getter
@Setter
public class BackupJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_time")
    private LocalDateTime backupTime;

    private String status;

    @Column(name = "file_path")
    private String filePath;

}