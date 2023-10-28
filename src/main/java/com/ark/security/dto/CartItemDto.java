package com.ark.security.dto;

import com.ark.security.models.Cart;
import com.ark.security.models.product.ProductDetail;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@Data
@Builder
public class CartItemDto implements Serializable {
    Integer id;
    Cart cart;
    String productName;
    ProductDetailDto productDetailDto;
    Integer quantity;
    Integer price;
    Date purchaseDate;
}
