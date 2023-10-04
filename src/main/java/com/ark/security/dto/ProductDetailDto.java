package com.ark.security.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductDetail}
 */
@Value
@Data
@Builder
public class ProductDetailDto implements Serializable {
    Integer id;
    Integer quantity;
    Integer price;
    Integer weight;
}