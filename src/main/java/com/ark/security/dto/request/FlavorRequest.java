package com.ark.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlavorRequest {
    @NotNull
    @NotBlank(message = "FLAVOR_NAME_NOT_EMPTY")
    String name;
    String description;
    Boolean status;
}
