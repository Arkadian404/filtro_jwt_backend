package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@JsonIdentityInfo(scope = ProductImage.class ,generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageName;
    private String url;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    //@JsonManagedReference(value = "product-image")
    private Product product;
}
