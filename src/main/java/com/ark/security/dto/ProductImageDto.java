package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductImage}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDto implements Serializable {
    Integer id;
    String imageName;
    String url;
}