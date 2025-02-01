package com.ark.security.models.product;

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
@Builder
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id", resolver = CustomResolver.class)
public class Flavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "flavor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> productList;
}
