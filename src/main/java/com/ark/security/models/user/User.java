package com.ark.security.models.user;

import com.ark.security.dto.UserDto;
import com.ark.security.models.Cart;
import com.ark.security.models.Employee;
import com.ark.security.models.Review;
import com.ark.security.models.token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    //why using @JsonBackReference here?
    //https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    private List<Token> tokens;


    @OneToOne(mappedBy = "user")
    @JsonBackReference(value = "user-employee")
    private Employee employee;

    @OneToMany
    @JsonBackReference(value = "user-cart")
    private List<Cart> carts;

    @OneToMany(mappedBy = "user")
    @JsonBackReference(value = "user-review")
    private List<Review> reviews;

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
