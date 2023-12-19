package com.ark.security.repository.product;

import com.ark.security.models.product.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    boolean existsVendorByName(String name);
}
