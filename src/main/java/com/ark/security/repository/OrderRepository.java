package com.ark.security.repository;

import com.ark.security.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, Integer> {

    Optional<List<Order>> findAllByUserId(int userId);

    Optional<Boolean> existsByOrderCode(String orderCode);

    Optional<Order> findByOrderCode(String orderCode);


    @Query(value = """
        select o.user_id as user_id, p.id as item_id, count(p.id) as preference from `order` as o 
        join order_detail as od on od.order_id = o.id
        join product_detail as pd on od.product_detail_id = pd.id
        join product as p on pd.product_id = p.id
        group by o.user_id, p.id 
        order by o.user_id, p.id
    """, nativeQuery = true)
    List<Object[]> findOrderMigrationData();





}
