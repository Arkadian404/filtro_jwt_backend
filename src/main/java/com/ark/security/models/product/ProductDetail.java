package com.ark.security.models.product;

import com.ark.security.dto.ProductDetailDto;
import com.ark.security.models.CartItem;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer stock;
    private Integer price;
    private Integer weight;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;



    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<CartItem> cartItem;



    public ProductDetailDto convertToDto(){
        return ProductDetailDto.builder()
                .id(this.id)
                .stock(this.stock)
                .price(this.price)
                .weight(this.weight)
                .build();
    }

}
