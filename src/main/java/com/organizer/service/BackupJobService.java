package com.organizer.service;

import com.organizer.entity.BackupJob;
import com.organizer.repository.BackupJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackupJobService {

    private final BackupJobRepository backupJobRepository;

    public List<BackupJob> findAll() {
        return backupJobRepository.findAll();
    }

    public Optional<BackupJob> findById(Long id) {
        return backupJobRepository.findById(id);
    }

    public BackupJob save(BackupJob backupJob) {
        return backupJobRepository.save(backupJob);
    }

    public BackupJob update(Long id, BackupJob updatedBackupJob) {
        BackupJob backupJob = backupJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Backup job not found"));

        backupJob.setBackupTime(updatedBackupJob.getBackupTime());
        backupJob.setStatus(updatedBackupJob.getStatus());
        backupJob.setFilePath(updatedBackupJob.getFilePath());

        return backupJobRepository.save(backupJob);
    }

    public void delete(Long id) {
        backupJobRepository.deleteById(id);
    }
}