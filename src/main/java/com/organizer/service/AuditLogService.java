package com.organizer.service;

import com.organizer.entity.AuditLog;
import com.organizer.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    public Optional<AuditLog> findById(Long id) {
        return auditLogRepository.findById(id);
    }

    public List<AuditLog> findByTableName(String tableName) {
        return auditLogRepository.findByTableName(tableName);
    }

    public List<AuditLog> findByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByActionTimeBetween(start, end);
    }

    public AuditLog save(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    public void delete(Long id) {
        auditLogRepository.deleteById(id);
    }
}