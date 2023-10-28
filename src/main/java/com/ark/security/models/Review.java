package com.ark.security.models;

import com.ark.security.dto.ReviewDto;
import com.ark.security.models.product.Product;
import com.ark.security.models.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    private Integer rating;
    private String comment;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Integer parentId;


    public ReviewDto  convertToDto(){
        return ReviewDto.builder()
                .id(this.id)
                .user(this.user.convertToDto())
                .product(this.product.convertToDto())
                .rating(this.rating)
                .comment(this.comment)
                .createdAt(this.createdAt.toString())
                .parentId(this.parentId)
                .build();
    }
}
