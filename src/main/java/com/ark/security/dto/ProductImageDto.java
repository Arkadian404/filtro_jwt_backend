package com.ark.security.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductImage}
 */
@Value
@Data
@Builder
public class ProductImageDto implements Serializable {
    Integer id;
    String imageName;
    String url;
}