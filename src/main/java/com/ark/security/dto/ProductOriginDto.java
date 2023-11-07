package com.ark.security.dto;

import com.ark.security.models.product.ProductOrigin;
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

    public ProductOrigin convertToEntity(){
        return ProductOrigin.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}