package com.ark.security.models.product;

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
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
}
