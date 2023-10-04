package com.ark.security.dto;

import com.ark.security.models.product.Flavor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Flavor}
 */
@Value
@Data
@Builder
public class FlavorDto implements Serializable {
    Integer id;
    String name;
}