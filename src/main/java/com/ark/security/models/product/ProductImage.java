package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.dto.ProductImageDto;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank(message = "Tên ảnh không được để trống")
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
//    @JsonManagedReference(value = "product-image")
    private Product product;


    public ProductImageDto convertToDto(){
        return ProductImageDto.builder()
                .id(this.id)
                .imageName(this.imageName)
                .url(this.url)
                .build();
    }
}
