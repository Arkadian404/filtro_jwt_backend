package com.ark.security.repository.product;


import com.ark.security.models.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsBrandByName(String name);
}
