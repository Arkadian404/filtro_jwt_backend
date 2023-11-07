package com.ark.security.models.product;

import com.ark.security.dto.ProductOriginDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String continent;
    private String description;

    @OneToMany(mappedBy = "origin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;


    public ProductOriginDto convertToDto(){
        return ProductOriginDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}


