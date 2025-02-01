package com.ark.security.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {
    String sort = "";
    String flavor = "";
    String brand = "";
    String category = "";
    String vendor = "";
    String origin = "";
}
