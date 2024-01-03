package com.ark.security.dto;

import com.ark.security.models.CartItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


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
    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
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
