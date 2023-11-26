package com.ark.security.dto;

import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.order.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Integer id;
    private String orderCode;
    private UserDto user;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String notes;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private ShippingMethodDto shippingMethod;
    private Integer total;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    public Order convertToEntity(){
        return Order.builder()
                .id(this.id)
                .user(this.user.convertToEntity())
                .fullName(this.fullName)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .province(this.province)
                .district(this.district)
                .ward(this.ward)
                .notes(this.notes)
                .paymentMethod(this.paymentMethod)
                .shippingMethod(this.shippingMethod.convertToEntity())
                .total(this.total)
                .orderDate(this.orderDate)
                .status(this.status)
                .build();
    }
}
