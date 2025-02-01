package com.ark.security.mapper;

import com.ark.security.dto.response.OrderDetailResponse;
import com.ark.security.models.order.OrderDetail;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    @AfterMapping
    default void setSpecificFields(OrderDetail orderDetail, @MappingTarget OrderDetailResponse orderDetailResponse){
        orderDetailResponse.setProductName(orderDetail.getProductDetail().getProduct().getName());
        orderDetailResponse.setProductSlug(orderDetail.getProductDetail().getProduct().getSlug());
    }
}
