package com.ark.security.dto;

import com.ark.security.models.product.Sale;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Sale}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDto implements Serializable {
    Integer id;
    Integer discount;

    public Sale convertToEntity(){
        return Sale.builder()
                .id(this.id)
                .discount(this.discount)
                .build();
    }
}