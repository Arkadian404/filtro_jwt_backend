package com.ark.security.models.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private String imageName;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
