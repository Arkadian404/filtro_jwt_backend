package com.ark.security.repository;

import com.ark.security.models.product.Category;
import com.ark.security.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findBySlug(String slug);

    @Query("""
        select p from Product p where p.name like %:name%
        """)
    Optional<List<Product>> searchByName(@Param("name") String name);

    Optional<List<Product>> findByOriginId(int id);

    Optional<List<Product>> findByIsSpecial(Boolean isSpecial);

    Optional<List<Product>> findByIsLimited(Boolean isLimited);

    boolean existsProductByName(String name);

    @Query("""
        select p from Product p join Category c on p.category.id = c.id where p.id =:id
""")
    Optional<Category> findCategoryUsingId(@Param("id") int id);

    Optional<List<Product>> findAllByCategoryId(int id);

    Optional<List<Product>> findAllByVendorId(int id);

    Optional<List<Product>> findAllByFlavorId(int id);

    Optional<List<Product>> findAllBySaleId(int id);

    //MYSQL QUERY
    @Query(value = "select * from Product as p order by p.created_at desc limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3LatestProducts();

    //MYSQL QUERY
    @Query(value = "select * from Product as p order by p.sold desc limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3BestSellerProducts();

    @Query(value = "select * from Product as p order by p.sold desc", nativeQuery = true)
    Optional<List<Product>> findBestSellerProducts();

    //MYSQL QUERY
    @Query(value = "select * from Product as p where p.is_special = true limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3SpecialProducts();

    @Query(value="select * from Product as p where p.product_origin_id =:id limit 10", nativeQuery = true)
    Optional<List<Product>> findByOriginName(@Param("id") int id);


    @Query(value = "select * from Product as p where p.category_id =:id  limit 10", nativeQuery = true)
    Optional<List<Product>> findTop10ProductsInSpecific(@Param("id") int id);

    @Query(value ="select * from Product  as p where p.id != :id and p.flavor_id = :flavorId   limit 10", nativeQuery = true)
    Optional<List<Product>> findTop10RelatedProductsByFlavor(@Param("id") int id, @Param("flavorId") int flavorId);


//    @Query(value = "select * from Product as p where p.", nativeQuery = true)
    Optional<List<Product>> findByOriginContinent(String continent);

    @Modifying
    @Query("""
        update Product p set p.status=false where p.id=:id
""")
    void softDeleteById(@Param("id") int id);

}
