package com.organizer.service;

import com.organizer.entity.Wedding;
import com.organizer.repository.WeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeddingService {

    private final WeddingRepository weddingRepository;

    public List<Wedding> findAll() {
        return weddingRepository.findAll();
    }

    public Optional<Wedding> findById(Long id) {
        return weddingRepository.findById(id);
    }

    public List<Wedding> findByStatus(String status) {
        return weddingRepository.findByStatus(status);
    }

    public List<Wedding> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return weddingRepository.findByWeddingDateBetween(startDate, endDate);
    }

    public Wedding save(Wedding wedding) {
        return weddingRepository.save(wedding);
    }

    public Wedding update(Long id, Wedding updatedWedding) {
        Wedding wedding = weddingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding not found"));

        wedding.setWeddingDate(updatedWedding.getWeddingDate());
        wedding.setStatus(updatedWedding.getStatus());
        wedding.setBudgetTotal(updatedWedding.getBudgetTotal());
        wedding.setClient(updatedWedding.getClient());

        return weddingRepository.save(wedding);
    }

    public void delete(Long id) {
        weddingRepository.deleteById(id);
    }
}