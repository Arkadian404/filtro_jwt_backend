package com.ark.security.repository;

import com.ark.security.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, Integer> {

    Optional<List<Order>> findAllByUserId(int userId);
    Optional<Boolean> existsByOrderCode(String orderCode);

    Optional<Order> findByOrderCode(String orderCode);
}
