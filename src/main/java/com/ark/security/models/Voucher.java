package com.ark.security.models;

import com.ark.security.models.product.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String code;

    private Double discount;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY)
    @JsonIgnore
    List<UserVoucher> userVoucherList;

    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Cart> cartList;
}
