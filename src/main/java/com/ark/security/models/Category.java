package com.ark.security.models;

import com.ark.security.config.CustomResolver;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id", resolver = CustomResolver.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Boolean status;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> productList;
}
