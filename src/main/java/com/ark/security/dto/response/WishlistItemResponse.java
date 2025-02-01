package com.ark.security.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistItemResponse {
    Integer id;
    WishlistResponse wishlist;
    ProductResponse product;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime addDate;
}
