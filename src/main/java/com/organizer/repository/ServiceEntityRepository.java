package com.organizer.repository;

import com.organizer.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findByVendorId(Long vendorId);

}