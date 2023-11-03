package com.ark.security.repository.product;

import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findAllByProductId(int id);

    Product findByProductId(int id);
}
