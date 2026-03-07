package com.organizer.controller;

import com.organizer.dto.AddVendorContractRequest;
import com.organizer.dto.AssignTaskRequest;
import com.organizer.dto.CreateWeddingRequest;
import com.organizer.dto.RegisterPaymentRequest;
import com.organizer.service.ProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @PostMapping("/weddings")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> createWedding(@RequestBody CreateWeddingRequest request) {
        procedureService.createWedding(request);
        return Map.of("status", "ok");
    }

    @PostMapping("/weddings/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> closeWedding(@PathVariable Long id) {
        procedureService.closeWedding(id);
        return Map.of("status", "ok");
    }

    @PostMapping("/contracts")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> addVendorContract(@RequestBody AddVendorContractRequest request) {
        procedureService.addVendorContract(request);
        return Map.of("status", "ok");
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> registerPayment(@RequestBody RegisterPaymentRequest request) {
        procedureService.registerPayment(request);
        return Map.of("status", "ok");
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> assignTask(@RequestBody AssignTaskRequest request) {
        procedureService.assignTask(request);
        return Map.of("status", "ok");
    }

    @PostMapping("/tasks/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER','ASSISTANT')")
    public Map<String, Object> completeTask(@PathVariable Long id) {
        procedureService.completeTask(id);
        return Map.of("status", "ok");
    }

    @PostMapping("/weddings/{id}/generate-initial-tasks")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Map<String, Object> generateInitialTasks(@PathVariable Long id) {
        procedureService.generateInitialTasks(id);
        return Map.of("status", "ok");
    }
}