package com.ark.security.dto;

import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
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
    private String slug;
    private BrandDto brand;
    private Double rating;
    private List<ProductDetailDto> productDetails;
    private String description;
    private List<ProductImageDto> images;
    private FlavorDto flavor;
    private CategoryDto category;
    private SaleDto sale;
    private ProductOriginDto origin;
    private VendorDto vendor;

    public Product convertToEntity(){
        List<ProductDetail> productDetails = this.productDetails.stream().map(ProductDetailDto::convertToEntity).toList();
        List<ProductImage> images = this.images.stream().map(ProductImageDto::convertToEntity).toList();
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .slug(this.slug)
                .brand(this.brand.convertToEntity())
                .rating(this.rating)
                .productDetails(productDetails)
                .description(this.description)
                .images(images)
                .flavor(this.flavor.convertToEntity())
                .category(this.category.convertToEntity())
                .sale(this.sale == null ? null : this.sale.convertToEntity())
                .origin(this.origin.convertToEntity())
                .vendor(this.vendor.convertToEntity())
                .build();
    }
}