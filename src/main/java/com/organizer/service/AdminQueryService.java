package com.organizer.service;

import com.organizer.entity.AuditLog;
import com.organizer.entity.User;
import com.organizer.repository.AuditLogRepository;
import com.organizer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminQueryService {

    private final JdbcTemplate jdbcTemplate;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    private static final int DEFAULT_LIMIT = 200;

    public List<Map<String, Object>> executeSelect(String sql, String userEmail) {

        if (sql == null || sql.isBlank()) {
            logAdminQuery(userEmail, "ADMIN_QUERY_ERROR");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQL is empty");
        }

        String normalized = normalize(sql);

        if (!(normalized.startsWith("select ") || normalized.startsWith("with "))) {
            logAdminQuery(userEmail, "ADMIN_QUERY_ERROR");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only SELECT queries are allowed");
        }

        if (containsDangerousKeywords(normalized)) {
            logAdminQuery(userEmail, "ADMIN_QUERY_ERROR");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forbidden SQL keywords detected");
        }

        if (normalized.contains(";")) {
            logAdminQuery(userEmail, "ADMIN_QUERY_ERROR");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Semicolons are not allowed");
        }

        String finalSql = addLimitIfMissing(sql, normalized);

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(finalSql);
            logAdminQuery(userEmail, "ADMIN_QUERY");
            return result;
        } catch (Exception ex) {
            logAdminQuery(userEmail, "ADMIN_QUERY_ERROR");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQL error: " + ex.getMessage());
        }
    }

    private String normalize(String sql) {
        return sql.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    private boolean containsDangerousKeywords(String normalized) {
        String[] banned = {
                "insert ", "update ", "delete ", "drop ", "alter ", "truncate ", "create ",
                "grant ", "revoke ", "call ", "execute ", "do ",
                "copy ", "vacuum ", "analyze ", "refresh ",
                "set ", "reset ", "comment ", "lock ",
                "--", "/*", "*/",
                "pg_read_file", "pg_ls_dir", "dblink"
        };

        for (String b : banned) {
            if (normalized.contains(b)) {
                return true;
            }
        }
        return false;
    }

    private String addLimitIfMissing(String originalSql, String normalized) {
        if (normalized.contains(" limit ")) {
            return originalSql;
        }
        return originalSql.trim() + " LIMIT " + DEFAULT_LIMIT;
    }

    private void logAdminQuery(String userEmail, String action) {
        Long userId = userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElse(null);

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setTableName("custom_sql");
        log.setRecordId(null);
        log.setActionTime(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}