package com.ark.security.models.order;

import com.ark.security.dto.OrderDto;
import com.ark.security.models.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`order`")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
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

    private String deliveryService;
    private Integer shippingFee;

    private Integer total;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public OrderDto convertToDto(){
        return OrderDto.builder()
                .id(this.id)
                .user(this.user.convertToDto())
                .orderCode(this.orderCode)
                .fullName(this.fullName)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .province(this.province)
                .district(this.district)
                .ward(this.ward)
                .notes(this.notes)
                .paymentMethod(this.paymentMethod)
                .deliveryService(this.deliveryService)
                .shippingFee(this.shippingFee)
                .total(this.total)
                .orderDate(this.orderDate)
                .status(this.status)
                .build();
    }

}
