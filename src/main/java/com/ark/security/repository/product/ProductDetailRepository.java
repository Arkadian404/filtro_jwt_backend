package com.ark.security.repository.product;

import com.ark.security.models.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    Optional<List<ProductDetail>> findAllByProductId(int id);

}
