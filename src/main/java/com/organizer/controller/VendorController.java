package com.organizer.controller;

import com.organizer.entity.Vendor;
import com.organizer.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public List<Vendor> getAll() {
        return vendorService.findAll();
    }

    @GetMapping("/{id}")
    public Vendor getById(@PathVariable Long id) {
        return vendorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    @GetMapping("/type/{serviceType}")
    public List<Vendor> getByServiceType(@PathVariable String serviceType) {
        return vendorService.findByServiceType(serviceType);
    }

    @PostMapping
    public Vendor create(@RequestBody Vendor vendor) {
        return vendorService.save(vendor);
    }

    @PutMapping("/{id}")
    public Vendor update(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.update(id, vendor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vendorService.delete(id);
    }
}