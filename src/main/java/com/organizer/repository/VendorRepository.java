package com.organizer.repository;

import com.organizer.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByServiceType(String serviceType);

}