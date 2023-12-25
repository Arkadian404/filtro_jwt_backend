package com.ark.security.models.product;

import com.ark.security.dto.ProductOriginDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank(message = "Tên xuất xứ không được để trống")
    private String name;
    @NotNull
    @NotBlank(message = "Tên châu lục không được để trống")
    private String continent;
    private String description;
    private Boolean status;

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


