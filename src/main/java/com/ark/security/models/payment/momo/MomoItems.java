package com.ark.security.models.payment.momo;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MomoItems {
    private String id;
    private String name;
    private String imageUrl;
    private Long price;
    private String currency = "VND";
    private Integer quantity;
    private Long totalPrice;
}
