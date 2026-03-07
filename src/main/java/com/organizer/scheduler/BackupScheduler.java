package com.organizer.scheduler;

import com.organizer.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private final BackupService backupService;

    // плановый backup по cron из properties
    @Scheduled(cron = "${backup.cron}")
    public void scheduledBackup() {
        backupService.createBackupFile();
    }
}