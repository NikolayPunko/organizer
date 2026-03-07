package com.organizer.repository;

import com.organizer.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByWeddingId(Long weddingId);

    List<Contract> findByStatus(String status);

}