package com.ark.security.models.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoDeliveryInfo {
    private String deliveryAddress;
    private String deliveryFee;
    private String quantity;

}
