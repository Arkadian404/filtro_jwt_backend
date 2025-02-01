package com.ark.security.mapper;

import com.ark.security.dto.request.WishlistRequest;
import com.ark.security.dto.response.WishlistResponse;
import com.ark.security.models.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    Wishlist toWishlist(Wishlist request);
    void updateWishlistFromRequest(@MappingTarget Wishlist wishlist, WishlistRequest request);
    @Mapping(source = "user.id", target = "userId")
    WishlistResponse toWishlistResponse(Wishlist wishlist);
}
