package com.ark.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoRequest {
    @NotBlank(message = "USER_FIRSTNAME_NOT_EMPTY")
    String firstname;
    @NotBlank(message = "USER_LASTNAME_NOT_EMPTY")
    String lastname;
    @NotBlank(message = "USER_EMAIL_NOT_EMPTY")
    @Email(message = "EMAIL_INVALID")
    String email;
    Date dob;
    String address;
    String province;
    String district;
    String ward;
    String phone;
}
