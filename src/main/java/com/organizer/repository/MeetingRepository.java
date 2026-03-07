package com.organizer.repository;

import com.organizer.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByWeddingId(Long weddingId);

    List<Meeting> findByMeetingDateBetween(LocalDateTime start, LocalDateTime end);

}