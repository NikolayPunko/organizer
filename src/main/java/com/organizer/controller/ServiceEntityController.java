package com.organizer.controller;

import com.organizer.entity.ServiceEntity;
import com.organizer.service.ServiceEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceEntityController {

    private final ServiceEntityService serviceEntityService;

    @GetMapping
    public List<ServiceEntity> getAll() {
        return serviceEntityService.findAll();
    }

    @GetMapping("/{id}")
    public ServiceEntity getById(@PathVariable Long id) {
        return serviceEntityService.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @GetMapping("/vendor/{vendorId}")
    public List<ServiceEntity> getByVendorId(@PathVariable Long vendorId) {
        return serviceEntityService.findByVendorId(vendorId);
    }

    @PostMapping
    public ServiceEntity create(@RequestBody ServiceEntity serviceEntity) {
        return serviceEntityService.save(serviceEntity);
    }

    @PutMapping("/{id}")
    public ServiceEntity update(@PathVariable Long id, @RequestBody ServiceEntity serviceEntity) {
        return serviceEntityService.update(id, serviceEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceEntityService.delete(id);
    }
}