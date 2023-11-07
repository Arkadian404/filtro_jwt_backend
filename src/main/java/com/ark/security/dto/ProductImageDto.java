package com.ark.security.dto;

import com.ark.security.models.product.ProductImage;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ark.security.models.product.ProductImage}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDto implements Serializable {
    Integer id;
    String imageName;
    String url;

    public ProductImage convertToEntity(){
        return ProductImage.builder()
                .id(this.id)
                .imageName(this.imageName)
                .url(this.url)
                .build();
    }
}