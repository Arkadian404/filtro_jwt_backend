package com.ark.security.repository;

import com.ark.security.models.product.ProductOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOriginRepository extends JpaRepository<ProductOrigin, Integer> {

    Optional<ProductOrigin> findByContinent(String continent);
}
