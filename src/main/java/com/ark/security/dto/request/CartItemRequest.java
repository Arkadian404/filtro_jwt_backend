package com.ark.security.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    Integer cartId;
    Integer productDetailId;
    @Positive(message = "CART_ITEM_MUST_BE_POSITIVE")
    Integer quantity;
    Integer price;
    Integer total;
}
