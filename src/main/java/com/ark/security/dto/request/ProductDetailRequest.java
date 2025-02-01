package com.ark.security.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailRequest {
    @NotNull
    @PositiveOrZero(message = "CAN_NOT_BE_NEGATIVE")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    Integer stock;
    @NotNull
    @PositiveOrZero(message = "CAN_NOT_BE_NEGATIVE")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    Integer price;
    @NotNull
    @PositiveOrZero(message = "CAN_NOT_BE_NEGATIVE")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    Integer weight;
    Boolean status;
    Integer productId;
}
