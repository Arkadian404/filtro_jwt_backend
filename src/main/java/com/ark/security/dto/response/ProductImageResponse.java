package com.ark.security.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
    Integer id;
    String imageName;
    String productName;
    String url;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Date createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Date updatedAt;
    Boolean status;
    //TODO: Add product id
    Integer productId;
    Integer categoryId;
}
