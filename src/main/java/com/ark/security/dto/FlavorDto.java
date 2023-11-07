package com.ark.security.dto;

import com.ark.security.models.product.Flavor;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Flavor}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlavorDto implements Serializable {
    Integer id;
    String name;

    public Flavor convertToEntity(){
        return Flavor.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}