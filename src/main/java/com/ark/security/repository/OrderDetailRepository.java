package com.ark.security.repository;

import com.ark.security.models.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Integer> {

    Optional<List<OrderDetail>> findByOrderId(int id);

}
