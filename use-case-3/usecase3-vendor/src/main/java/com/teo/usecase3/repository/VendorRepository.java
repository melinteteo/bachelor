package com.teo.usecase3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teo.usecase3.entity.Vendor;

@Repository	
public interface VendorRepository extends JpaRepository<Vendor, Long>{

}
