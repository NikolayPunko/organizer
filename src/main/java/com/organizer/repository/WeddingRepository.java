package com.organizer.repository;

import com.organizer.entity.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeddingRepository extends JpaRepository<Wedding, Long> {

    List<Wedding> findByStatus(String status);

    List<Wedding> findByWeddingDateBetween(LocalDate startDate, LocalDate endDate);

}