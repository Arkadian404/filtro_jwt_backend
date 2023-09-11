package com.ark.security.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer quantity;
    private Integer sold;
    private Integer price;
    private String description;
    private String image;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "flavor_id")
    //@JsonManagedReference(value = "flavor-product")
    private Flavor flavor;

    @ManyToOne
    @JoinColumn(name = "category_id")
    //@JsonManagedReference(value = "category-product")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    //@JsonManagedReference(value = "sale-product")
    private Sale sale;

}
