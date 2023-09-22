package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.models.Category;
import com.ark.security.models.Flavor;
import com.ark.security.models.Sale;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
//@JsonIdentityInfo(scope = Product.class,generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties(ignoreUnknown = true, value ={"images"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer quantity;
    private Integer sold;
    private Integer price;
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    //@JsonManagedReference(value = "product-image")
    private List<ProductImage> images;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Boolean status;
    private Boolean isSpecial;
    private String origin;

    @ManyToOne
    @JoinColumn(name = "flavor_id")
    //@JsonBackReference(value = "flavor-product")
    private Flavor flavor;

    @ManyToOne
    @JoinColumn(name = "category_id")
    //@JsonBackReference(value = "category-product")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    //@JsonBackReference(value = "sale-product")
    private Sale sale;

}
