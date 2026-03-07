package com.organizer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class ProcedureRepository {

    private final JdbcTemplate jdbcTemplate;

    public void createWedding(Long clientId, LocalDate weddingDate, BigDecimal budget, Long userId) {
        jdbcTemplate.update(
                "CALL sp_create_wedding(?, ?, ?, ?)",
                clientId,
                Date.valueOf(weddingDate),
                budget,
                userId
        );
    }

    public void closeWedding(Long weddingId) {
        jdbcTemplate.update(
                "CALL sp_close_wedding(?)",
                weddingId
        );
    }

    public void addVendorContract(Long weddingId, Long serviceId, BigDecimal price) {
        jdbcTemplate.update(
                "CALL sp_add_vendor_contract(?, ?, ?)",
                weddingId,
                serviceId,
                price
        );
    }

    public void registerPayment(Long weddingId, Long contractId, BigDecimal amount, String paymentType) {
        jdbcTemplate.update(
                "CALL sp_register_payment(?, ?, ?, ?)",
                weddingId,
                contractId,
                amount,
                paymentType
        );
    }

    public void assignTask(Long weddingId, String title, Long memberId, LocalDate dueDate) {
        jdbcTemplate.update(
                "CALL sp_assign_task(?, ?, ?, ?)",
                weddingId,
                title,
                memberId,
                Date.valueOf(dueDate)
        );
    }

    public void completeTask(Long taskId) {
        jdbcTemplate.update(
                "CALL sp_complete_task(?)",
                taskId
        );
    }

    public void generateInitialTasks(Long weddingId) {
        jdbcTemplate.update(
                "CALL sp_generate_initial_tasks(?)",
                weddingId
        );
    }
}