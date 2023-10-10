package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductDetail}
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto implements Serializable {
    private Integer id;
    private Integer quantity;
    private Integer price;
    private Integer weight;
}