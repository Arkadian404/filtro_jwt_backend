package com.ark.security.models;

import com.ark.security.dto.WishlistDto;
import com.ark.security.dto.WishlistItemDto;
import com.ark.security.models.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wishlist")
    @JsonIgnore
    private List<WishlistItem> wishlistItems;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    private Boolean status;


    public WishlistDto convertToDto(){
        return WishlistDto.builder()
                .id(this.id)
                .user(this.user.convertToDto())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .status(this.status)
                .build();
    }

}
