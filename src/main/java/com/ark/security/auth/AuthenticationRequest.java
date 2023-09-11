package com.ark.security.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
}
