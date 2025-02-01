package com.ark.security.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    Integer productId;
    Integer userId;
    @Min(value = 1, message = "RATING_NOT_VALID")
    @Max(value = 5, message = "RATING_NOT_VALID")
    Integer rating;
    @NotBlank(message="COMMENT_NOT_EMPTY")
    String comment;
    Integer parentId;
}
