package com.ark.security.models;

import com.ark.security.dto.VoucherDto;
import com.ark.security.models.product.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
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
    @PositiveOrZero(message = "Giảm giá phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Giảm giá tối đa là 100")
    private Double discount;
    private String description;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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

    public VoucherDto convertToDto(){
        return VoucherDto.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .discount(this.discount)
                .description(this.description)
                .expirationDate(this.expirationDate)
                .category(this.category != null ? this.category.convertToDto() : null)
                .build();
    }


}
