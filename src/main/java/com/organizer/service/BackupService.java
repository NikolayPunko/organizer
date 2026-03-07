package com.organizer.service;

import com.organizer.entity.BackupJob;
import com.organizer.repository.BackupJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BackupService {

    private final BackupJobRepository backupJobRepository;

    @Value("${backup.pgDumpPath}")
    private String pgDumpPath;

    @Value("${backup.psqlPath}")
    private String psqlPath;

    @Value("${backup.host}")
    private String host;

    @Value("${backup.port}")
    private String port;

    @Value("${backup.dbName}")
    private String dbName;

    @Value("${backup.user}")
    private String user;

    @Value("${backup.password}")
    private String password;

    @Value("${backup.dir}")
    private String backupDir;

    public Path createBackupFile() {
        BackupJob job = createJob("BACKUP_STARTED", null);

        try {
            Files.createDirectories(Paths.get(backupDir));

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "backup_" + dbName + "_" + ts + ".sql";
            Path outFile = Paths.get(backupDir).resolve(fileName);

            List<String> cmd = List.of(
                    pgDumpPath,
                    "-h", host,
                    "-p", port,
                    "-U", user,
                    "-d", dbName,
                    "--clean",
                    "--if-exists",
                    "--no-owner",
                    "--no-privileges",
                    "-f", outFile.toAbsolutePath().toString()
            );

            runProcess(cmd);

            if (!Files.exists(outFile) || Files.size(outFile) == 0) {
                saveJobResult(job, "BACKUP_FAILED", outFile.toAbsolutePath().toString());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Backup file is empty");
            }

            saveJobResult(job, "BACKUP_SUCCESS", outFile.toAbsolutePath().toString());
            return outFile;

        } catch (IOException e) {
            saveJobResult(job, "BACKUP_FAILED", null);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Backup failed: " + e.getMessage());
        } catch (RuntimeException e) {
            saveJobResult(job, "BACKUP_FAILED", null);
            throw e;
        }
    }

    public byte[] readBackup(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Read backup failed: " + e.getMessage());
        }
    }

    public void restoreFromUploadedFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".sql")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only .sql files are allowed");
        }

        BackupJob job = createJob("RESTORE_STARTED", originalName);

        Path tempSql = null;

        try {
            Files.createDirectories(Paths.get(backupDir));

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            tempSql = Paths.get(backupDir).resolve("restore_" + ts + ".sql");

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, tempSql, StandardCopyOption.REPLACE_EXISTING);
            }

            List<String> cmd = List.of(
                    psqlPath,
                    "-h", host,
                    "-p", port,
                    "-U", user,
                    "-d", dbName,
                    "-f", tempSql.toAbsolutePath().toString()
            );

            runProcess(cmd);

            saveJobResult(job, "RESTORE_SUCCESS", tempSql.toAbsolutePath().toString());

        } catch (IOException e) {
            saveJobResult(job, "RESTORE_FAILED", tempSql != null ? tempSql.toAbsolutePath().toString() : originalName);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Restore failed: " + e.getMessage());
        } catch (RuntimeException e) {
            saveJobResult(job, "RESTORE_FAILED", tempSql != null ? tempSql.toAbsolutePath().toString() : originalName);
            throw e;
        } finally {
            if (tempSql != null) {
                try {
                    Files.deleteIfExists(tempSql);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void runProcess(List<String> cmd) {
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", password);

            pb.redirectErrorStream(true);

            Process p = pb.start();

            String out;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                out = sb.toString();
            }

            int code = p.waitFor();
            if (code != 0) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Command failed (" + code + "):\n" + out
                );
            }

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Process error: " + e.getMessage());
        }
    }

    private BackupJob createJob(String status, String filePath) {
        BackupJob job = new BackupJob();
        job.setBackupTime(LocalDateTime.now());
        job.setStatus(status);
        job.setFilePath(filePath);
        return backupJobRepository.save(job);
    }

    private void saveJobResult(BackupJob job, String status, String filePath) {
        job.setBackupTime(LocalDateTime.now());
        job.setStatus(status);
        job.setFilePath(filePath);
        backupJobRepository.save(job);
    }
}