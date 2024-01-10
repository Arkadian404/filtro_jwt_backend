package com.ark.security.dto;

import com.ark.security.models.user.User;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link com.ark.security.models.user.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Date dob;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;

    public User convertToEntity(){
        return User.builder()
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