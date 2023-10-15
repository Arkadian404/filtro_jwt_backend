package com.ark.security.models;

import com.ark.security.models.product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@JsonIdentityInfo(scope = CartItem.class ,generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    private Integer quantity;
    private Integer price;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;


}
