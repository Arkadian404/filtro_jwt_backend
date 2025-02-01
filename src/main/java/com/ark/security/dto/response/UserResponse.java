package com.ark.security.dto.response;

import com.ark.security.models.user.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String firstname;
    String lastname;
    String username;
    String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date dob;
    String address;
    String province;
    String district;
    String ward;
    String phone;
    @Enumerated(EnumType.STRING)
    Role role;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime updatedDate;
}
