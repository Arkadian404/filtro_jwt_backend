package com.ark.security.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String fullName;
    String email;
    String phone;
    String address;
    String province;
    String district;
    String ward;
    String notes;
    String paymentMethod;
    String deliveryService;
    Integer shippingFee;
    Integer total;
    String status;
}
