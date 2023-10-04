package com.ark.security.dto;

import com.ark.security.models.product.Sale;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Sale}
 */
@Value
@Data
@Builder
public class SaleDto implements Serializable {
    Integer id;
    Integer discount;
}