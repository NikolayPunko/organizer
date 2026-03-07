package com.organizer.repository;

import com.organizer.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByWeddingId(Long weddingId);

    List<Task> findByAssignedToId(Long memberId);

    List<Task> findByStatus(String status);

    List<Task> findByDueDateBeforeAndStatusNot(LocalDate date, String status);

}