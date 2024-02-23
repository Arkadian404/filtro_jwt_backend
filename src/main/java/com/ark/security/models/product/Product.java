package com.ark.security.models.product;

import com.ark.security.dto.ProductDetailDto;
import com.ark.security.dto.ProductDto;
import com.ark.security.dto.ProductImageDto;
import com.ark.security.models.WishlistItem;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true, value ={"images"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    private String slug;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    private Integer sold;
    @Max(value = 5, message = "Đánh giá tối đa là 5 sao")
    private Double rating;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductDetail> productDetails;

    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    @JsonBackReference(value = "product-image")
//    @JsonIgnore
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
    @JsonIgnore
    private List<WishlistItem> wishlistItems;

    @OneToMany(mappedBy = "product")
//    @JsonBackReference
    @JsonIgnore
    private List<Review> reviews;


    public ProductDto convertToDto(){
        List<ProductDetailDto> productDetailDtos = this.productDetails.stream().map(ProductDetail::convertToDto).toList();
        List<ProductImageDto> productImageDtos = this.images.stream().map(ProductImage::convertToDto).toList();
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .slug(this.slug)
                .brand(this.brand.convertToDto())
                .rating(this.rating)
                .productDetails(productDetailDtos)
                .description(this.description)
                .images(productImageDtos)
                .flavor(this.flavor.convertToDto())
                .category(this.category.convertToDto())
                .sale(this.sale == null ? null : this.sale.convertToDto())
                .origin(this.origin.convertToDto())
                .vendor(this.vendor.convertToDto())
                .build();
    }

}
