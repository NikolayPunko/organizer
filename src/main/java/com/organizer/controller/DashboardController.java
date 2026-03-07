package com.organizer.controller;

import com.organizer.dto.DashboardResponse;
import com.organizer.repository.ClientRepository;
import com.organizer.repository.MeetingRepository;
import com.organizer.repository.PaymentRepository;
import com.organizer.repository.TaskRepository;
import com.organizer.repository.WeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER','ASSISTANT')")
public class DashboardController {

    private final WeddingRepository weddingRepository;
    private final ClientRepository clientRepository;
    private final TaskRepository taskRepository;
    private final MeetingRepository meetingRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping
    public DashboardResponse getDashboard() {
        long weddingsCount = weddingRepository.count();
        long clientsCount = clientRepository.count();
        long overdueTasksCount = taskRepository
                .findByDueDateBeforeAndStatusNot(LocalDate.now(), "DONE")
                .size();
        long meetingsCount = meetingRepository.count();
        long paymentsCount = paymentRepository.count();

        return new DashboardResponse(
                weddingsCount,
                clientsCount,
                overdueTasksCount,
                meetingsCount,
                paymentsCount
        );
    }
}