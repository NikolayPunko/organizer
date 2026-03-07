package com.organizer.controller;

import com.organizer.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
public class ReportController {

    private final ReportRepository repo;

    @GetMapping("/wedding-finance")
    public List<Map<String, Object>> weddingFinance() {
        return repo.weddingFinance();
    }

    @GetMapping("/overdue-tasks")
    public List<Map<String, Object>> overdueTasks() {
        return repo.overdueTasks();
    }

    @GetMapping("/team-workload")
    public List<Map<String, Object>> teamWorkload() {
        return repo.teamWorkload();
    }

    @GetMapping("/vendor-totals")
    public List<Map<String, Object>> vendorTotals() {
        return repo.vendorTotals();
    }

    @GetMapping("/weddings-by-status")
    public List<Map<String, Object>> weddingsByStatus() {
        return repo.weddingsByStatus();
    }

    @GetMapping("/vendor-payments")
    public List<Map<String, Object>> vendorPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return repo.vendorPayments(start, end);
    }

    @GetMapping("/payments-by-type")
    public List<Map<String, Object>> paymentsByType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return repo.paymentsByType(start, end);
    }

    @GetMapping("/contracts-by-status")
    public List<Map<String, Object>> contractsByStatus() {
        return repo.contractsByStatus();
    }

    @GetMapping("/meetings-by-period")
    public List<Map<String, Object>> meetingsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return repo.meetingsByPeriod(start, end);
    }

    @GetMapping("/audit-summary")
    public List<Map<String, Object>> auditSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return repo.auditSummary(start, end);
    }
}