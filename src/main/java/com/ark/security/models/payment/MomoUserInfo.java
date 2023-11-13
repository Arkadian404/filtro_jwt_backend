package com.ark.security.models.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoUserInfo {
    private String name;
    private String phoneNumber;
    private String email;
}
