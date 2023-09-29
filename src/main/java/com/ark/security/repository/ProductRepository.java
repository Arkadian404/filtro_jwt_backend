package com.ark.security.repository;

import com.ark.security.models.Category;
import com.ark.security.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    Optional<List<Product>> findByName(String name);

    Optional<List<Product>> findByOriginId(int id);

    Optional<List<Product>> findByIsSpecial(Boolean isSpecial);

    boolean existsProductByName(String name);

    @Query("""
        select p from Product p join Category c on p.category.id = c.id where p.id =:id
""")
    Optional<Category> findCategoryUsingId(@Param("id") int id);

    Optional<List<Product>> findAllByCategoryId(int id);

    Optional<List<Product>> findAllByVendorId(int id);

    Optional<List<Product>> findAllByFlavorId(int id);

    Optional<List<Product>> findAllBySaleId(int id);


    @Modifying
    @Query("""
        update Product p set p.status=false where p.id=:id
""")
    void softDeleteById(@Param("id") int id);

}
