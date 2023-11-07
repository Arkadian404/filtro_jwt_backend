package com.ark.security.dto;

import com.ark.security.models.WishlistItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistItemDto {
    private Integer id;
    private WishlistDto wishlist;
    private ProductDto product;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addDate;

    public WishlistItem convertToEntity(){
        return WishlistItem.builder()
                .id(this.id)
                .wishlist(this.wishlist.convertToEntity())
                .product(this.product.convertToEntity())
                .addDate(this.addDate)
                .build();
    }
}
