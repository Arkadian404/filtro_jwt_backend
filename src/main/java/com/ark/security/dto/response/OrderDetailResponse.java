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
public class OrderDetailResponse {
    Integer id;
    OrderResponse order;
    String productName;
    String productSlug;
    ProductDetailResponse productDetail;
    ProductImageResponse productImage;
    Integer quantity;
    Integer price;
    Integer total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDate;

}
