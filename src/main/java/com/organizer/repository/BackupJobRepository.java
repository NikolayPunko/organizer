package com.organizer.repository;

import com.organizer.entity.BackupJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupJobRepository extends JpaRepository<BackupJob, Long> {
}