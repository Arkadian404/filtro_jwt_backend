package com.ark.security.dto;

import com.ark.security.models.product.Brand;
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

    public Brand convertToEntity(){
        return Brand.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}