package com.ark.security.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.Brand}
 */
@Value
public class BrandDto implements Serializable {
    Integer id;
    String name;
}