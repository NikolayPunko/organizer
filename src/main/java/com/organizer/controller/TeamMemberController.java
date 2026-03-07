package com.organizer.controller;

import com.organizer.entity.TeamMember;
import com.organizer.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @GetMapping
    public List<TeamMember> getAll() {
        return teamMemberService.findAll();
    }

    @GetMapping("/{id}")
    public TeamMember getById(@PathVariable Long id) {
        return teamMemberService.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));
    }

    @PostMapping
    public TeamMember create(@RequestBody TeamMember teamMember) {
        return teamMemberService.save(teamMember);
    }

    @PutMapping("/{id}")
    public TeamMember update(@PathVariable Long id, @RequestBody TeamMember teamMember) {
        return teamMemberService.update(id, teamMember);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teamMemberService.delete(id);
    }
}