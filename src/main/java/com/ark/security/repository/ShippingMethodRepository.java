package com.ark.security.repository;

import com.ark.security.models.order.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {


}
