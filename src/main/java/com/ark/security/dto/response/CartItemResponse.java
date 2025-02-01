package com.ark.security.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Integer id;
    String productName;
    String slug;
    ProductImageResponse productImage;
    ProductDetailResponse productDetail;
    CartResponse cart;
    Integer quantity;
    Integer price;
    Integer total;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime purchaseDate;
}
