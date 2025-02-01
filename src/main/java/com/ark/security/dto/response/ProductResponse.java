package com.ark.security.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String name;
    String slug;
    String description;
    Double rating;
    Integer sold;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime updatedAt;
    Boolean isSpecial;
    Boolean isLimited;
    Boolean status;
    List<ProductDetailResponse> productDetails;
    List<ProductImageResponse> images;
    BrandResponse brand;
    CategoryResponse category;
    FlavorResponse flavor;
    VendorResponse vendor;
    ProductOriginResponse productOrigin;
}
