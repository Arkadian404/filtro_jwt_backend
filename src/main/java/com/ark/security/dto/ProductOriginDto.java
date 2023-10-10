package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductOrigin}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOriginDto implements Serializable {
    Integer id;
    String name;
}