package com.ark.security.dto;

import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {
        private Integer id;
        private OrderDto order;
        private String productName;
        private String productSlug;
        private ProductDetailDto productDetail;
        private ProductImageDto productImage;
        private Integer quantity;
        private Integer price;
        private Integer total;
        @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderDate;

        public OrderDetail convertToEntity(){
            return OrderDetail.builder()
                    .id(this.id)
                    .order(this.order.convertToEntity())
                    .productDetail(this.productDetail.convertToEntity())
                    .quantity(this.quantity)
                    .price(this.price)
                    .total(this.total)
                    .orderDate(this.orderDate)
                    .build();
        }
}
