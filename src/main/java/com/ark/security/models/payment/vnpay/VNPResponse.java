package com.ark.security.models.payment.vnpay;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPResponse {
    private String status;
    private String message;
    private String paymentUrl;
}
