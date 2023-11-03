package com.ark.security.repository.product;

import com.ark.security.models.product.Review;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<List<Review>> findByParentId(int id);
    Optional<List<Review>> findByProductId(int id);
}
