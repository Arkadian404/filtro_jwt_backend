package com.ark.security.dto;

import com.ark.security.models.product.Vendor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Vendor}
 */
@Value
@Data
@Builder
public class VendorDto implements Serializable {
    Integer id;
    String name;
}