package com.ark.security.dto;

import lombok.*;

import java.io.Serializable;
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
}