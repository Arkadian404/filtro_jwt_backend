package com.ark.security.repository;
import com.ark.security.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
    List<CartItem> findAllByCartId(int id);

    @Query("""
        select t from CartItem t where t.cart.user.id = :userId and t.productDetail.id = :productDetailId
""")
    List<CartItem> checkCartItemExist(int productDetailId, int userId);
}
