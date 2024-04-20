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

    @Query(value = """
    SELECT 
        CASE 
            WHEN :parentId IS NULL THEN EXISTS (SELECT 1 FROM review WHERE user_id = :userId AND product_id = :productId AND parent_id IS NULL) 
            ELSE EXISTS (SELECT 1 FROM review WHERE user_id = :userId AND product_id = :productId AND parent_id = :parentId) 
        END
""", nativeQuery = true)
    Long isReviewExisting(int userId, int productId, Integer parentId);

    Boolean existsByUserIdAndProductIdAndParentIdIsNull(int userId, int productId);

}
