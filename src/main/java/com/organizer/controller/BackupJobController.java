package com.organizer.controller;

import com.organizer.entity.BackupJob;
import com.organizer.service.BackupJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupJobController {

    private final BackupJobService backupJobService;

    @GetMapping
    public List<BackupJob> getAll() {
        return backupJobService.findAll();
    }

    @GetMapping("/{id}")
    public BackupJob getById(@PathVariable Long id) {
        return backupJobService.findById(id)
                .orElseThrow(() -> new RuntimeException("Backup job not found"));
    }

    @PostMapping
    public BackupJob create(@RequestBody BackupJob backupJob) {
        return backupJobService.save(backupJob);
    }

    @PutMapping("/{id}")
    public BackupJob update(@PathVariable Long id, @RequestBody BackupJob backupJob) {
        return backupJobService.update(id, backupJob);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        backupJobService.delete(id);
    }
}