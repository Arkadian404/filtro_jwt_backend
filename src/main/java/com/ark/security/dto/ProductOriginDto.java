package com.ark.security.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductOrigin}
 */
@Value
@Data
@Builder
public class ProductOriginDto implements Serializable {
    Integer id;
    String name;
}