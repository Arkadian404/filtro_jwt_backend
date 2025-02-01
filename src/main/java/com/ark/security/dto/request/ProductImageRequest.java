package com.ark.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {
    @NotNull
    @NotBlank(message = "PRODUCT_IMAGE_NAME_NOT_EMPTY")
    String imageName;
    String url;
    Boolean status;
    Integer productId;
}
