package com.ark.security.models.product;

import com.ark.security.dto.ProductDetailDto;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.OrderDetail;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank(message = "Số lượng không được để trống")
    private Integer stock;
    @NotNull
    @NotBlank(message = "Giá không được để trống")
    private Integer price;
    @NotNull
    @NotBlank(message = "Cân nặng không được để trống")
    private Integer weight;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<CartItem> cartItem;

    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<OrderDetail> orderDetail;



    public ProductDetailDto convertToDto(){
        int categoryId = this.product.getCategory().getId();
        return ProductDetailDto.builder()
                .id(this.id)
                .stock(this.stock)
                .price(this.price)
                .weight(this.weight)
                .categoryId(categoryId)
                .build();
    }

}
