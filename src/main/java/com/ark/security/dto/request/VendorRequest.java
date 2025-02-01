package com.ark.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorRequest {
    @NotNull
    @NotBlank(message = "VENDOR_NAME_NOT_EMPTY")
    String name;
    @NotNull
    @NotBlank(message = "VENDOR_ADDRESS_NOT_EMPTY")
    String address;
    @NotNull
    @NotBlank(message = "VENDOR_PHONE_NOT_EMPTY")
//    @Pattern(regexp="\\b\\d{3}[-.]?\\\\d{3}[-.]?\\\\d{4}\\\\b", message = "PHONE_INVALID")
    String phone;
    @NotBlank(message = "VENDOR_EMAIL_NOT_EMPTY")
    @Email(message = "EMAIL_INVALID")
    String email;
    String description;
    Boolean status;
}
