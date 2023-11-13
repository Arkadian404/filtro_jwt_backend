package com.ark.security.repository;
import com.ark.security.models.Cart;
import com.ark.security.models.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(int userId);
    Cart findByUserIdAndStatus(int userId, boolean status);
    @Query("""
        select c from Cart c where  c.user.username = :username and c.status = true
        """)
    Optional<Cart> findByUserUsername(String username);

}
