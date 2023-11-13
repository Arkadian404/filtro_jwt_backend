package com.ark.security.dto;

import com.ark.security.models.Wishlist;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDto {
    private Integer id;
    private UserDto user;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Boolean status;


    public Wishlist convertToEntity(){
        return Wishlist.builder()
                .id(this.id)
                .user(this.user.convertToEntity())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .status(this.status)
                .build();
    }
}
