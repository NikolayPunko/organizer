package com.organizer.controller;

import com.organizer.entity.AuditLog;
import com.organizer.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLog> getAll() {
        return auditLogService.findAll();
    }

    @GetMapping("/{id}")
    public AuditLog getById(@PathVariable Long id) {
        return auditLogService.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
    }

    @GetMapping("/table/{tableName}")
    public List<AuditLog> getByTableName(@PathVariable String tableName) {
        return auditLogService.findByTableName(tableName);
    }

    @GetMapping("/action/{action}")
    public List<AuditLog> getByAction(@PathVariable String action) {
        return auditLogService.findByAction(action);
    }

    @GetMapping("/range")
    public List<AuditLog> getByDateRange(@RequestParam LocalDateTime start,
                                         @RequestParam LocalDateTime end) {
        return auditLogService.findByDateRange(start, end);
    }
}