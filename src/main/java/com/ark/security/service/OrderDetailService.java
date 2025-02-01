package com.ark.security.service;

import com.ark.security.dto.response.OrderDetailResponse;
import com.ark.security.mapper.OrderDetailMapper;
import com.ark.security.mapper.ProductDetailMapper;
import com.ark.security.mapper.ProductImageMapper;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductDetailMapper productDetailMapper;

    public List<OrderDetail> getOrderDetailByOrderId(int id){
        return orderDetailRepository.findByOrderId(id).orElse(null);
    }

    public List<OrderDetailResponse> getOrderDetailResponseByOrderId(int id){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id).orElse(null);
        if(orderDetails!=null){
            return orderDetails.stream()
                    .map(orderDetail -> {
                        OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
                        orderDetailResponse.setProductDetail(productDetailMapper.toProductDetailResponse(orderDetail.getProductDetail()));
                        orderDetailResponse.setProductImage(productImageMapper.toProductImageResponse(orderDetail.getProductDetail().getProduct().getImages().get(0)));
                        return orderDetailResponse;
                    }).toList();
        }
        return Collections.emptyList();
    }

}
