package com.ark.security.models.order;

import com.ark.security.dto.OrderDetailDto;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
    private Integer price;
    private Integer total;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;


    public OrderDetailDto convertToDto(){
        String productName = this.productDetail.getProduct().getName();
        ProductImage productImage = this.productDetail.getProduct().getImages().get(0);
        return OrderDetailDto.builder()
                .id(this.id)
                .order(this.order.convertToDto())
                .productName(productName)
                .productImage(productImage.convertToDto())
                .productDetail(this.productDetail.convertToDto())
                .quantity(this.quantity)
                .price(this.price)
                .total(this.total)
                .orderDate(this.orderDate)
                .build();
    }
}
