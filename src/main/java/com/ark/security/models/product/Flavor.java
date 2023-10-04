package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.dto.FlavorDto;
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
public class Flavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "flavor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> productList;

    public FlavorDto convertToDto(){
        return FlavorDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

}
