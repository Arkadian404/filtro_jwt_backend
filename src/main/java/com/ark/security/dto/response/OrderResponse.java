package com.ark.security.dto.response;

import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.order.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    UserResponse user;
    String orderCode;
    String fullName;
    String email;
    String phone;
    String address;
    String province;
    String district;
    String ward;
    String notes;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
    String deliveryService;
    Integer shippingFee;
    Integer total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    Double discount;
}
