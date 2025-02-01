package com.ark.security.mapper;

import com.ark.security.dto.request.CartItemRequest;
import com.ark.security.dto.response.CartItemResponse;
import com.ark.security.models.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest cartItemRequest);
    void updateCartItem(@MappingTarget CartItem cartItem, CartItemRequest cartItemRequest);
    CartItemResponse toCartItemResponse(CartItem cartItem);


    @AfterMapping
    default void setSpecificFields(@MappingTarget CartItemResponse response, CartItem cartItem){
        response.setProductName(cartItem.getProductDetail().getProduct().getName());
        response.setSlug(cartItem.getProductDetail().getProduct().getSlug());
    }
}
