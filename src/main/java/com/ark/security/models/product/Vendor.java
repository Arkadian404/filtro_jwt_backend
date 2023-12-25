package com.ark.security.models.product;

import com.ark.security.dto.VendorDto;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String name;
    @NotNull
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    @NotNull
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    @NotNull
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;


    public VendorDto convertToDto(){
        return VendorDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

}
