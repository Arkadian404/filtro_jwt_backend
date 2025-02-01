package com.ark.security.mapper;

import com.ark.security.dto.request.OrderRequest;
import com.ark.security.dto.response.OrderResponse;
import com.ark.security.models.order.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest orderRequest);
    OrderResponse toOrderResponse(Order order);
}
