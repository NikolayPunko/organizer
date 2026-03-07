package com.organizer.service;

import com.organizer.entity.Meeting;
import com.organizer.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public List<Meeting> findAll() {
        return meetingRepository.findAll();
    }

    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }

    public List<Meeting> findByWeddingId(Long weddingId) {
        return meetingRepository.findByWeddingId(weddingId);
    }

    public List<Meeting> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return meetingRepository.findByMeetingDateBetween(start, end);
    }

    public Meeting save(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Meeting update(Long id, Meeting updatedMeeting) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setMeetingDate(updatedMeeting.getMeetingDate());
        meeting.setLocation(updatedMeeting.getLocation());
        meeting.setNotes(updatedMeeting.getNotes());
        meeting.setWedding(updatedMeeting.getWedding());

        return meetingRepository.save(meeting);
    }

    public void delete(Long id) {
        meetingRepository.deleteById(id);
    }
}