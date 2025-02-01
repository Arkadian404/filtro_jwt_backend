package com.ark.security.models.product;

import com.ark.security.models.CartItem;
import com.ark.security.models.order.OrderDetail;
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
//    @JsonManagedReference
    private Product product;

    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<CartItem> cartItem;

    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<OrderDetail> orderDetail;
}
