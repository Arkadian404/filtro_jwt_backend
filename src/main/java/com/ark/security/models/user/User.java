package com.ark.security.models.user;

import com.ark.security.dto.UserDto;
import com.ark.security.models.Cart;
import com.ark.security.models.Employee;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Wishlist;
import com.ark.security.models.order.Order;
import com.ark.security.models.product.Review;
import com.ark.security.models.token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Họ không được null")
    @NotBlank(message = "Họ không được để trống")
    private String firstname;
    @NotNull(message = "Tên không được null")
    @NotBlank(message = "Tên không được để trống")
    private String lastname;
    @NotNull(message = "Tên đăng nhập không được null")
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;
    @NotNull(message = "Email không được null")
    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;
    private String password;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
    private Date dob;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;
    private Boolean enabled;

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
        if(this.enabled == null || !this.enabled){
            return false;
        }

        return true;
    }

    public UserDto convertToDto(){
        return UserDto.builder()
                .id(this.id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .username(this.username)
                .email(this.email)
                .dob(this.dob)
                .address(this.address)
                .province(this.province)
                .district(this.district)
                .ward(this.ward)
                .phone(this.phone)
                .build();
    }

}
