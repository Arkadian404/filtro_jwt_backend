package com.ark.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewDto {
    private Integer id;
    private UserDto user;
    private ProductDto product;
    private Integer rating;
    private String comment;
    private String createdAt;
    private Integer parentId;
}
