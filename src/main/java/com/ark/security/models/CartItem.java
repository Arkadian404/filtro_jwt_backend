package com.ark.security.models;

import com.ark.security.dto.CartItemDto;
import com.ark.security.dto.ProductImageDto;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    private Integer quantity;
    private Integer price;
    private Integer total;
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime purchaseDate;

    public CartItemDto convertToDto(){
        ProductImage productImage = this.productDetail.getProduct().getImages().get(0);
        String productName = this.productDetail.getProduct().getName();
        String slug = this.productDetail.getProduct().getSlug();
        ProductImageDto productImageDto = productImage.convertToDto();
        return CartItemDto.builder()
                .id(this.id)
                .cart(this.cart.convertToDto())
                .productName(productName)
                .slug(slug)
                .productImage(productImageDto)
                .productDetail(this.productDetail.convertToDto())
                .quantity(this.quantity)
                .price(this.price)
                .total(this.total)
                .purchaseDate(this.purchaseDate)
                .build();
    }

}