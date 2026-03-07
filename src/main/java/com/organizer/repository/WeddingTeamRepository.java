package com.organizer.repository;

import com.organizer.entity.WeddingTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeddingTeamRepository extends JpaRepository<WeddingTeam, Long> {

    List<WeddingTeam> findByWeddingId(Long weddingId);

    List<WeddingTeam> findByMemberId(Long memberId);

}