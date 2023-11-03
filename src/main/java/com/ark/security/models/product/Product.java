package com.ark.security.models.product;

import com.ark.security.dto.ProductDetailDto;
import com.ark.security.dto.ProductDto;
import com.ark.security.dto.ProductImageDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String slug;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    private Integer sold;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductDetail> productDetails;

    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImage> images;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Boolean status;
    private Boolean isSpecial;
    private Boolean isLimited;

    @ManyToOne
    @JoinColumn(name = "flavor_id")
    private Flavor flavor;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_origin_id")
    private ProductOrigin origin;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Review> reviews;


    public ProductDto convertToDto(){
        List<ProductDetailDto> productDetailDtos = this.productDetails.stream().map(ProductDetail::convertToDto).toList();
        List<ProductImageDto> productImageDtos = this.images.stream().map(ProductImage::convertToDto).toList();
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .slug(this.slug)
                .brand(this.brand.convertToDto())
                .productDetails(productDetailDtos)
                .description(this.description)
                .images(productImageDtos)
                .flavor(this.flavor.convertToDto())
                .category(this.category.convertToDto())
                .sale(this.sale.convertToDto())
                .origin(this.origin.convertToDto())
                .vendor(this.vendor.convertToDto())
                .build();
    }

}
