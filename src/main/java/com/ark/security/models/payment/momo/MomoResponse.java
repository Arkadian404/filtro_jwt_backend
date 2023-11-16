package com.ark.security.models.payment.momo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoResponse {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private Long amount;
    private Long responseTime;
    private String message;
    private Integer resultCode;
    private String payUrl;
}
