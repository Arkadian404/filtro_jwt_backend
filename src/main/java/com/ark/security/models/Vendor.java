package com.ark.security.models;

import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

}
