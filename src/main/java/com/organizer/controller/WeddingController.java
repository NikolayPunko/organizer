package com.organizer.controller;

import com.organizer.dto.WeddingDetailsResponse;
import com.organizer.entity.Wedding;
import com.organizer.service.WeddingDetailsService;
import com.organizer.service.WeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weddings")
@RequiredArgsConstructor
public class WeddingController {

    private final WeddingService weddingService;
    private final WeddingDetailsService weddingDetailsService;

    @GetMapping
    public List<Wedding> getAll() {
        return weddingService.findAll();
    }

    @GetMapping("/{id}")
    public Wedding getById(@PathVariable Long id) {
        return weddingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding not found"));
    }

    @GetMapping("/status/{status}")
    public List<Wedding> getByStatus(@PathVariable String status) {
        return weddingService.findByStatus(status);
    }

    @GetMapping("/range")
    public List<Wedding> getByDateRange(@RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) {
        return weddingService.findByDateRange(startDate, endDate);
    }

    @PostMapping
    public Wedding create(@RequestBody Wedding wedding) {
        return weddingService.save(wedding);
    }

    @PutMapping("/{id}")
    public Wedding update(@PathVariable Long id, @RequestBody Wedding wedding) {
        return weddingService.update(id, wedding);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        weddingService.delete(id);
    }

    @GetMapping("/{id}/details")
    public WeddingDetailsResponse getDetails(@PathVariable Long id) {
        return weddingDetailsService.getDetails(id);
    }
}