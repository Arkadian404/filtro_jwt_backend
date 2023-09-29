package com.ark.security.models.product;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
//@JsonIdentityInfo(scope = ProductDetail.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;
    private Integer price;
    private Integer sold;
    private Integer weight;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
