package com.ark.security.dto;

import com.ark.security.models.product.Category;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
@Data
@Builder
public class CategoryDto implements Serializable {
    Integer id;
    String name;
}