package com.ark.security.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOriginResponse {
    Integer id;
    String name;
    String continent;
    String description;
    Boolean status;
}
