package com.organizer.controller;

import com.organizer.entity.Meeting;
import com.organizer.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public List<Meeting> getAll() {
        return meetingService.findAll();
    }

    @GetMapping("/{id}")
    public Meeting getById(@PathVariable Long id) {
        return meetingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    @GetMapping("/wedding/{weddingId}")
    public List<Meeting> getByWeddingId(@PathVariable Long weddingId) {
        return meetingService.findByWeddingId(weddingId);
    }

    @GetMapping("/range")
    public List<Meeting> getByDateRange(@RequestParam LocalDateTime start,
                                        @RequestParam LocalDateTime end) {
        return meetingService.findByDateRange(start, end);
    }

    @PostMapping
    public Meeting create(@RequestBody Meeting meeting) {
        return meetingService.save(meeting);
    }

    @PutMapping("/{id}")
    public Meeting update(@PathVariable Long id, @RequestBody Meeting meeting) {
        return meetingService.update(id, meeting);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        meetingService.delete(id);
    }
}