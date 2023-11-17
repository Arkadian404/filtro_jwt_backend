package com.ark.security.models.order;

import com.ark.security.dto.ShippingMethodDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer fee;
    private String duration;

    @OneToMany(mappedBy = "shippingMethod")
    @JsonBackReference
    private List<Order> orders;

    public ShippingMethodDto convertToDto(){
        return ShippingMethodDto.builder()
                .id(this.id)
                .name(this.name)
                .fee(this.fee)
                .duration(this.duration)
                .build();
    }

}
