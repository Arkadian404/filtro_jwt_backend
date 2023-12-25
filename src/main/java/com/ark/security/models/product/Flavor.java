package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.dto.FlavorDto;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @NotBlank(message = "Tên hương vị không được để trống")
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
