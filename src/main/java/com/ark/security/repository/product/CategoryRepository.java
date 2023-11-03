package com.ark.security.repository.product;

import com.ark.security.models.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);


    @Query("""
    select c from Category c order by c.id limit 5
""")
    List<Category> find5Categories();

    @Modifying
    @Query("""
    update Category c set c.status = false where c.id =:id
    """)
    void softDeleteById(@Param("id") int id);
}
