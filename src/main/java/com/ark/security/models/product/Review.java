package com.ark.security.models.product;

import com.ark.security.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


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
    private Instant createdAt;
    private Integer parentId;
}
