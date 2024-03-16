package com.ark.security.repository.product;

import com.ark.security.models.product.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<List<Review>> findByParentId(int id);
    Optional<List<Review>> findByProductId(int id);

    @Query(value = "select avg(rating) from review where product_id =:id and parent_id is null group by product_id", nativeQuery = true)
    Double avgRatingByProductId(int id);

    @Query(value = "select count(user_id) from review where product_id =:id and parent_id is null group by product_id", nativeQuery = true)
    Integer countReviewByProductId(int id);

    Boolean existsByUserIdAndProductId(int userId, int productId);

}
