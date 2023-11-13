package com.ark.security.dto;

import com.ark.security.models.product.ProductDetail;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductDetail}
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto implements Serializable {
    private Integer id;
    private Integer stock;
    private Integer price;
    private Integer weight;
    private Integer categoryId;

    public ProductDetail convertToEntity(){
        return ProductDetail.builder()
                .id(this.id)
                .stock(this.stock)
                .price(this.price)
                .weight(this.weight)
                .build();
    }
}