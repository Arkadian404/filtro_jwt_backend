package com.ark.security.models.product;

import com.ark.security.models.WishlistItem;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String slug;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    private Integer sold;
    @Max(value = 5, message = "Đánh giá tối đa là 5 sao")
    private Double rating;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductDetail> productDetails;

    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductImage> images;

    private LocalDateTime createdAt;
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
}
