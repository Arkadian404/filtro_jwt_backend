package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.Brand}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDto implements Serializable {
    private Integer id;
    private String name;
}