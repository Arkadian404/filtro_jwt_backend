package com.ark.security.mapper;

import com.ark.security.dto.request.WishlistItemRequest;
import com.ark.security.dto.response.WishlistItemResponse;

import com.ark.security.models.WishlistItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WishlistItemMapper {
    WishlistItem toWishlistItem(WishlistItemRequest request);
    void updateWishlistItem(@MappingTarget WishlistItem wishlistItem, WishlistItem request);
    WishlistItemResponse toWishlistItemResponse(WishlistItem wishlistItem);

    @AfterMapping
    default void setAddDate(@MappingTarget WishlistItemResponse wishlistItemResponse, WishlistItem wishlistItem){
        wishlistItemResponse.setAddDate(wishlistItem.getAddDate());
    }
}
