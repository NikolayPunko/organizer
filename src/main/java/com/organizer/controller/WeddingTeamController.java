package com.organizer.controller;

import com.organizer.entity.WeddingTeam;
import com.organizer.service.WeddingTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wedding-team")
@RequiredArgsConstructor
public class WeddingTeamController {

    private final WeddingTeamService weddingTeamService;

    @GetMapping
    public List<WeddingTeam> getAll() {
        return weddingTeamService.findAll();
    }

    @GetMapping("/{id}")
    public WeddingTeam getById(@PathVariable Long id) {
        return weddingTeamService.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding team entry not found"));
    }

    @GetMapping("/wedding/{weddingId}")
    public List<WeddingTeam> getByWeddingId(@PathVariable Long weddingId) {
        return weddingTeamService.findByWeddingId(weddingId);
    }

    @GetMapping("/member/{memberId}")
    public List<WeddingTeam> getByMemberId(@PathVariable Long memberId) {
        return weddingTeamService.findByMemberId(memberId);
    }

    @PostMapping
    public WeddingTeam create(@RequestBody WeddingTeam weddingTeam) {
        return weddingTeamService.save(weddingTeam);
    }

    @PutMapping("/{id}")
    public WeddingTeam update(@PathVariable Long id, @RequestBody WeddingTeam weddingTeam) {
        return weddingTeamService.update(id, weddingTeam);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        weddingTeamService.delete(id);
    }
}