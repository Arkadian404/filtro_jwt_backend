package com.ark.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RefreshToken {
    @JsonProperty("refreshToken")
    private String refreshToken;
}
