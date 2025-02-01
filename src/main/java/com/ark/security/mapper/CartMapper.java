package com.ark.security.mapper;

import com.ark.security.dto.request.CartRequest;
import com.ark.security.dto.response.CartResponse;
import com.ark.security.models.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartRequest cartRequest);
    void updateCart(@MappingTarget Cart cart, CartRequest cartRequest);
    @Mapping(target = "userId", source = "user.id")
    CartResponse toCartResponse(Cart cart);

}
