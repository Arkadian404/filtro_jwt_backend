package com.ark.security.models;

import com.ark.security.dto.WishlistItemDto;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime addDate;


    public WishlistItemDto convertToDto(){
        ProductImage productImage = this.productDetail.getProduct().getImages().get(0);
        String productName = this.productDetail.getProduct().getName();
        String slug = this.productDetail.getProduct().getSlug();
        return WishlistItemDto.builder()
                .id(this.id)
                .wishlist(this.wishlist.convertToDto())
                .productName(productName)
                .slug(slug)
                .productImage(productImage.convertToDto())
                .productDetail(this.productDetail.convertToDto())
                .addDate(this.addDate)
                .build();
    }

}
