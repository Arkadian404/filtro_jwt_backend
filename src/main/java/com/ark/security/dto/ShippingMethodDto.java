package com.ark.security.dto;

import com.ark.security.models.order.ShippingMethod;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingMethodDto {
    private Integer id;
    private String name;
    private Integer fee;
    private String duration;

    public ShippingMethod convertToEntity(){
        return ShippingMethod.builder()
                .id(this.id)
                .name(this.name)
                .fee(this.fee)
                .duration(this.duration)
                .build();
    }
}
