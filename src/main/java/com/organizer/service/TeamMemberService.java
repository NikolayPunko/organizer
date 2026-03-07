package com.organizer.service;

import com.organizer.entity.TeamMember;
import com.organizer.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    public List<TeamMember> findAll() {
        return teamMemberRepository.findAll();
    }

    public Optional<TeamMember> findById(Long id) {
        return teamMemberRepository.findById(id);
    }

    public TeamMember save(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    public TeamMember update(Long id, TeamMember updatedMember) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        member.setFullName(updatedMember.getFullName());
        member.setPosition(updatedMember.getPosition());
        member.setPhone(updatedMember.getPhone());
        member.setEmail(updatedMember.getEmail());

        return teamMemberRepository.save(member);
    }

    public void delete(Long id) {
        teamMemberRepository.deleteById(id);
    }
}