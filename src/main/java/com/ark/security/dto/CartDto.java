package com.ark.security.dto;

import com.ark.security.models.Cart;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto implements Serializable {
    private Integer id;
    private UserDto user;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Integer total;
    private VoucherDto voucher;

    public Cart convertToEntity(){
        return Cart.builder()
                .id(this.id)
                .user(this.user.convertToEntity())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .total(this.total)
                .build();
    }
}
