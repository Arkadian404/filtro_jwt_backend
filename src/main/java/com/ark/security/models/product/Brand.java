package com.ark.security.models.product;

import com.ark.security.dto.BrandDto;
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
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Product> products;

    public BrandDto convertToDto(){
        return BrandDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
