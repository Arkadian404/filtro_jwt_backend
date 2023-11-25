package com.ark.security.service;

import com.ark.security.dto.OrderDetailDto;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailByOrderId(int id){
        return orderDetailRepository.findByOrderId(id).orElse(null);
    }

    public List<OrderDetailDto> getOrderDetailDtoByOrderId(int id){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id).orElse(null);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        if(orderDetails!=null){
            orderDetails.forEach(orderDetail -> orderDetailDtos.add(orderDetail.convertToDto()));
            return orderDetailDtos;
        }
        return Collections.emptyList();
    }


    public void saveOrderDetail(OrderDetail orderDetail){
        orderDetailRepository.save(orderDetail);
    }

}
