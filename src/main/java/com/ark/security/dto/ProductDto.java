package com.ark.security.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.ark.security.models.product.Product}
 */
@Value
@Data
@Builder
public class ProductDto implements Serializable {
    Integer id;
    String name;
    List<ProductDetailDto> productDetails;
    String description;
    List<ProductImageDto> images;
    FlavorDto flavor;
    CategoryDto category;
    SaleDto sale;
    ProductOriginDto origin;
    VendorDto vendor;
}