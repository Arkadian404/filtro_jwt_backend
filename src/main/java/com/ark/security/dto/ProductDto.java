package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * DTO for {@link com.ark.security.models.product.Product}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto implements Serializable {
    private Integer id;
    private String name;
    private List<ProductDetailDto> productDetails;
    private String description;
    private List<ProductImageDto> images;
    private FlavorDto flavor;
    private CategoryDto category;
    private SaleDto sale;
    private ProductOriginDto origin;
    private VendorDto vendor;


    public Comparator<ProductDto> byPrice(){
        return Comparator.comparing(p -> p.productDetails.get(0).getPrice());
    }
}