package com.ark.security.dto;

import com.ark.security.models.product.Vendor;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Vendor}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorDto implements Serializable {
    Integer id;
    String name;

    public Vendor convertToEntity(){
        return Vendor.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}