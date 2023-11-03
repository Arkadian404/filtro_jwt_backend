package com.ark.security.repository.product;

import com.ark.security.models.product.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Modifying
    @Query("""
        update Sale s set s.status=false where s.id=:id
""")
    void softDeleteById(@Param("id") int id);
}
