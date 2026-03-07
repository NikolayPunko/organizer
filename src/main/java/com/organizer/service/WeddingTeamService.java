package com.organizer.service;

import com.organizer.entity.WeddingTeam;
import com.organizer.repository.WeddingTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeddingTeamService {

    private final WeddingTeamRepository weddingTeamRepository;

    public List<WeddingTeam> findAll() {
        return weddingTeamRepository.findAll();
    }

    public Optional<WeddingTeam> findById(Long id) {
        return weddingTeamRepository.findById(id);
    }

    public List<WeddingTeam> findByWeddingId(Long weddingId) {
        return weddingTeamRepository.findByWeddingId(weddingId);
    }

    public List<WeddingTeam> findByMemberId(Long memberId) {
        return weddingTeamRepository.findByMemberId(memberId);
    }

    public WeddingTeam save(WeddingTeam weddingTeam) {
        return weddingTeamRepository.save(weddingTeam);
    }

    public WeddingTeam update(Long id, WeddingTeam updatedWeddingTeam) {
        WeddingTeam weddingTeam = weddingTeamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding team entry not found"));

        weddingTeam.setRole(updatedWeddingTeam.getRole());
        weddingTeam.setWedding(updatedWeddingTeam.getWedding());
        weddingTeam.setMember(updatedWeddingTeam.getMember());

        return weddingTeamRepository.save(weddingTeam);
    }

    public void delete(Long id) {
        weddingTeamRepository.deleteById(id);
    }
}