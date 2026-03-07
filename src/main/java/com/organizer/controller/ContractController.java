package com.organizer.controller;

import com.organizer.entity.Contract;
import com.organizer.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public List<Contract> getAll() {
        return contractService.findAll();
    }

    @GetMapping("/{id}")
    public Contract getById(@PathVariable Long id) {
        return contractService.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @GetMapping("/wedding/{weddingId}")
    public List<Contract> getByWeddingId(@PathVariable Long weddingId) {
        return contractService.findByWeddingId(weddingId);
    }

    @GetMapping("/status/{status}")
    public List<Contract> getByStatus(@PathVariable String status) {
        return contractService.findByStatus(status);
    }

    @PostMapping
    public Contract create(@RequestBody Contract contract) {
        return contractService.save(contract);
    }

    @PutMapping("/{id}")
    public Contract update(@PathVariable Long id, @RequestBody Contract contract) {
        return contractService.update(id, contract);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contractService.delete(id);
    }
}