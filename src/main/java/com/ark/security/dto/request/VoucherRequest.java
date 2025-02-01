package com.ark.security.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    @NotBlank(message = "VOUCHER_NAME_NOT_EMPTY")
    String name;
    @PositiveOrZero(message = "VOUCHER_DISCOUNT_NOT_VALID")
    @Max(value = 100, message = "VOUCHER_DISCOUNT_NOT_VALID")
    Double discount;
    String description;
    LocalDateTime expirationDate;
    Integer categoryId;
}
