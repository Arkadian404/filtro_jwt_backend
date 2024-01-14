package com.ark.security.models;

import com.ark.security.dto.CartDto;
import com.ark.security.models.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    private Integer total;
    private Boolean status;

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private Voucher voucher;


    public CartDto convertToDto(){
        return CartDto.builder()
                .id(this.id)
                .user(this.user.convertToDto())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .total(this.total)
                .voucher(this.voucher != null ? this.voucher.convertToDto() : null)
                .build();
    }
}
