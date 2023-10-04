package com.ark.security.repository;

import com.ark.security.models.product.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
}
