package com.organizer.service;

import com.organizer.entity.ServiceEntity;
import com.organizer.repository.ServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceEntityService {

    private final ServiceEntityRepository serviceEntityRepository;

    public List<ServiceEntity> findAll() {
        return serviceEntityRepository.findAll();
    }

    public Optional<ServiceEntity> findById(Long id) {
        return serviceEntityRepository.findById(id);
    }

    public List<ServiceEntity> findByVendorId(Long vendorId) {
        return serviceEntityRepository.findByVendorId(vendorId);
    }

    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceEntityRepository.save(serviceEntity);
    }

    public ServiceEntity update(Long id, ServiceEntity updatedServiceEntity) {
        ServiceEntity serviceEntity = serviceEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        serviceEntity.setServiceName(updatedServiceEntity.getServiceName());
        serviceEntity.setBasePrice(updatedServiceEntity.getBasePrice());
        serviceEntity.setVendor(updatedServiceEntity.getVendor());

        return serviceEntityRepository.save(serviceEntity);
    }

    public void delete(Long id) {
        serviceEntityRepository.deleteById(id);
    }
}