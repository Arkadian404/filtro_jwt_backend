package com.ark.security.models.user;

import com.ark.security.models.Cart;
import com.ark.security.models.Employee;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Wishlist;
import com.ark.security.models.order.Order;
import com.ark.security.models.product.Review;
import com.ark.security.models.token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "users")
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private Date dob;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean enabled;
    private String accountId;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    //why using @JsonBackReference here?
    //https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    private List<Token> tokens;


    @OneToOne(mappedBy = "user")
    @JsonBackReference(value = "user-employee")
    private Employee employee;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "cart-user")
    private List<Cart> carts;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user-wishlist")
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user-order")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user-review")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user-voucher")
    private List<UserVoucher> userVoucherList;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled != null && this.enabled;
    }
}
