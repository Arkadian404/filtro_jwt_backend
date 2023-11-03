package com.ark.security.dto;

import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto implements Serializable {
    private Integer id;
    private CartDto cart;
    private String productName;
    private String slug;
    private ProductImageDto productImage;
    private ProductDetailDto productDetail;
    private Integer  quantity;
    private Integer price;
    private Integer total;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    public CartItem convertToEntity(){
        return CartItem.builder()
                .id(this.id)
                .cart(this.cart.convertToEntity())
                .productDetail(this.productDetail.convertToEntity())
                .quantity(this.quantity)
                .price(this.price)
                .total(this.total)
                .purchaseDate(this.purchaseDate)
                .build();
    }
}
