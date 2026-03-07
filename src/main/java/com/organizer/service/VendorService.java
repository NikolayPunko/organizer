package com.organizer.service;

import com.organizer.entity.Vendor;
import com.organizer.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    public Optional<Vendor> findById(Long id) {
        return vendorRepository.findById(id);
    }

    public List<Vendor> findByServiceType(String serviceType) {
        return vendorRepository.findByServiceType(serviceType);
    }

    public Vendor save(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Vendor update(Long id, Vendor updatedVendor) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setName(updatedVendor.getName());
        vendor.setServiceType(updatedVendor.getServiceType());
        vendor.setPhone(updatedVendor.getPhone());
        vendor.setEmail(updatedVendor.getEmail());

        return vendorRepository.save(vendor);
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }
}