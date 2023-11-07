package com.ark.security.repository;

import com.ark.security.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {
    Optional<List<WishlistItem>> findAllByWishlistId(int id);

    Optional<WishlistItem> findByProductId(int id);

    void deleteByProductId(int id);
}
