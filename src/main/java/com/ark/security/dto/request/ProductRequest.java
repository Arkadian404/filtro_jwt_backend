package com.ark.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotNull
    @NotBlank(message = "PRODUCT_NAME_NOT_EMPTY")
    String name;
    String description;
    Boolean isSpecial;
    Boolean isLimited;
    Boolean status;
    Integer brandId;
    Integer categoryId;
    Integer flavorId;
    Integer vendorId;
    Integer originId;
}
