package com.ark.security.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorResponse {
    Integer id;
    String name;
    String address;
    String phone;
    String email;
    String description;
    Boolean status;
}
