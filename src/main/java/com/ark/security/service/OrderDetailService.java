package com.ark.security.service;

import com.ark.security.models.order.OrderDetail;
import com.ark.security.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailByOrderId(int id){
        return orderDetailRepository.findByOrderId(id).orElse(null);
    }


    public void saveOrderDetail(OrderDetail orderDetail){
        orderDetailRepository.save(orderDetail);
    }

}
