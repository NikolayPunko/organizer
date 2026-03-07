package com.organizer.repository;

import com.organizer.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTableName(String tableName);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByActionTimeBetween(LocalDateTime start, LocalDateTime end);

}