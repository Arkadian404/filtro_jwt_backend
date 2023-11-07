package com.ark.security.dto;

import com.ark.security.models.product.Category;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto implements Serializable {
    private Integer id;
    private String name;

    public Category convertToEntity(){
        return Category.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}