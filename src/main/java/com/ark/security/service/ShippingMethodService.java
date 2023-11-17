package com.ark.security.service;

import com.ark.security.models.order.ShippingMethod;
import com.ark.security.repository.ShippingMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;

    public List<ShippingMethod> getAllShippingMethods(){
        return shippingMethodRepository.findAll();
    }

}
