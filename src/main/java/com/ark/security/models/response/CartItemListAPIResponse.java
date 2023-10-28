package com.ark.security.models.response;

import com.ark.security.dto.CartItemDto;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemListAPIResponse {
    private List<CartItemDto> cartItemList;
    private List<ProductImage> productImages;
}
