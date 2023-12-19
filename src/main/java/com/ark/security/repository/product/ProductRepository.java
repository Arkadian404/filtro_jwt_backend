package com.ark.security.repository.product;

import com.ark.security.models.product.Category;
import com.ark.security.models.product.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findBySlug(String slug);

    @Query("""
        select p from Product p where p.name like %:name% and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
        """)
    Optional<List<Product>> searchByName(@Param("name") String name);

    @Query("""
        select p from Product p where p.origin.id =:id and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
""")
    Optional<List<Product>> findByOriginId(int id);

    @Query("""
            select p from Product p where p.isSpecial =:isSpecial  and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
    """)
    Optional<List<Product>> findByIsSpecial(Boolean isSpecial);

    @Query("""
            select p from Product p where p.isLimited =:isLimited  and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
    """)
    Optional<List<Product>> findByIsLimited(Boolean isLimited);

    boolean existsProductByName(String name);

    @Query("""
        select p from Product p join Category c on p.category.id = c.id where p.id =:id
""")
    Optional<Category> findCategoryUsingId(@Param("id") int id);

    @Query("""
        select p from Product p where p.category.id =:id and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
""")
    Optional<List<Product>> findAllByCategoryId(int id);

    @Query("""
        select p from Product p where p.vendor.id =:id and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
""")
    Optional<List<Product>> findAllByVendorId(int id);

    @Query("""
        select p from Product p where p.flavor.id =:id and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
""")
    Optional<List<Product>> findAllByFlavorId(int id);

    Optional<List<Product>> findAllBySaleId(int id);

    //MYSQL QUERY
    @Query(value = "select * from product as p where p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) " +
            "order by p.created_at desc limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3LatestProducts();

    //MYSQL QUERY
    @Query(value = "select * from product as p where p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) " +
            "order by p.sold desc limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3BestSellerProducts();

    @Query(value = "select * from product as p where p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) " +
            "order by p.sold desc", nativeQuery = true)
    Optional<List<Product>> findBestSellerProducts();

    //MYSQL QUERY
    @Query(value = "select * from product as p where p.is_special = true " +
            "and p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) limit 3", nativeQuery = true)
    Optional<List<Product>> findTop3SpecialProducts();

    @Query(value="select * from product as p where p.product_origin_id =:id " +
            "and p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) limit 10", nativeQuery = true)
    Optional<List<Product>> findByOriginName(@Param("id") int id);


    @Query(value = "select * from product as p where p.category_id =:id " +
            "and p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) limit 10", nativeQuery = true)
    Optional<List<Product>> findTop10ProductsInSpecific(@Param("id") int id);

    @Query(value ="select * from product as p where p.id != :id and p.flavor_id =:flavorId " +
            "and p.flavor_id in (select f.id from flavor as f where f.status = true ) " +
            "and p.category_id in (select c.id from category as c where c.status = true) " +
            "and p.vendor_id in (select v.id from  vendor as v where v.status = true) " +
            "and p.product_origin_id in (select o.id from product_origin as o where o.status = true) " +
            "and p.brand_id in (select b.id from brand as b where b.status = true) limit 10", nativeQuery = true)
    Optional<List<Product>> findTop10RelatedProductsByFlavor(@Param("id") int id, @Param("flavorId") int flavorId);


    @Query("""
        select p from Product p where p.origin.continent =:continent and p.category.status = true and p.flavor.status = true and p.vendor.status = true and p.origin.status = true and p.brand.status = true
""")
    Optional<List<Product>> findByOriginContinent(String continent);

    @Modifying
    @Query("""
        update Product p set p.status=false where p.id=:id
""")
    void softDeleteById(@Param("id") int id);

}
