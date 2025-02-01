package com.ark.security.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    Integer id;
    String productName;
    Integer stock;
    Integer price;
    Integer weight;
    Boolean status;
    //TODO: Add product id
    Integer productId;
    Integer categoryId;
}
