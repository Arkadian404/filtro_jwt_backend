package com.ark.security.models;

import com.ark.security.models.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@JsonIdentityInfo(scope = Cart.class, generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Cart {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    private Integer total;
    private Boolean status;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    @JsonIgnore
    //@JsonManagedReference(value = "product-image")
    private List<CartItem> cartItems;

}
